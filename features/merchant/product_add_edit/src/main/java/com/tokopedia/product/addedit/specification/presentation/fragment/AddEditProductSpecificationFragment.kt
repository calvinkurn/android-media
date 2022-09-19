package com.tokopedia.product.addedit.specification.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXTRA_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.common.util.SharedPreferencesUtil
import com.tokopedia.product.addedit.common.util.setDescriptionClick
import com.tokopedia.product.addedit.common.util.setFragmentToUnifyBgColor
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.specification.di.DaggerAddEditProductSpecificationComponent
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryData
import com.tokopedia.product.addedit.specification.presentation.adapter.SpecificationValueAdapter
import com.tokopedia.product.addedit.specification.presentation.dialog.NewUserSpecificationBottomSheet
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.product.addedit.specification.presentation.viewmodel.AddEditProductSpecificationViewModel
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import javax.inject.Inject

class AddEditProductSpecificationFragment: BaseDaggerFragment() {

    companion object {
        fun createInstance(cacheManagerId: String): Fragment {
            return AddEditProductSpecificationFragment().apply {
                arguments = Bundle().apply { putString(EXTRA_CACHE_MANAGER_ID, cacheManagerId) }
            }
        }
    }

    @Inject
    lateinit var viewModel: AddEditProductSpecificationViewModel

    private var tickerSpecification: Ticker? = null
    private var loaderSpecification: LoaderUnify? = null
    private var rvSpecification: RecyclerView? = null
    private var btnSpecification: UnifyButton? = null

    private var tvDeleteAll: TextView? = null
    private var specificationValueAdapter: SpecificationValueAdapter? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerAddEditProductSpecificationComponent
                .builder()
                .addEditProductComponent(AddEditProductComponentBuilder
                        .getComponent(requireContext().applicationContext as BaseMainApplication))
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cacheManagerId = arguments?.getString(EXTRA_CACHE_MANAGER_ID).orEmpty()
        val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)
        val productInputModel = saveInstanceCacheManager.get(EXTRA_PRODUCT_INPUT_MODEL,
                    ProductInputModel::class.java, ProductInputModel())

        viewModel.setProductInputModel(productInputModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_add_edit_product_specification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set bg color programatically, to reduce overdraw
        setFragmentToUnifyBgColor()

        // setup UI
        setupViews(view)
        setupToolbarActions()
        setupSubmitButton()
        setupTicker()

        // setup observers
        observeProductInputModel()
        observeAnnotationCategoryData()
        observeErrorMessage()
        observeValidateSpecificationInputModelResult()
    }

    private fun setupViews(view: View) {
        tickerSpecification = view.findViewById(R.id.tickerSpecification)
        loaderSpecification = view.findViewById(R.id.loaderSpecification)
        rvSpecification = view.findViewById(R.id.rvSpecification)
        btnSpecification = view.findViewById(R.id.btnSpecification)
    }

    private fun setupTicker() {
        val isInfoDisplayed = SharedPreferencesUtil.getFirstTimeSpecification(requireActivity())
        val htmlDescription = getString(R.string.label_info_specification)
        val newUserSpecificationBottomSheet = NewUserSpecificationBottomSheet()
        newUserSpecificationBottomSheet.setOnDismissListener {
            activity?.let {
                SharedPreferencesUtil.setFirstTimeSpecification(it, true)
            }
        }

        tickerSpecification?.setHtmlDescription(htmlDescription)
        tickerSpecification?.setDescriptionClick {
            newUserSpecificationBottomSheet.show(childFragmentManager)
        }

        if (!isInfoDisplayed) {
            newUserSpecificationBottomSheet.show(childFragmentManager)
        }
    }

    private fun observeProductInputModel() {
        viewModel.productInputModel.observe(viewLifecycleOwner, {
            viewModel.getSpecifications(it.detailInputModel.categoryId)
        })
    }

    private fun observeValidateSpecificationInputModelResult() {
        viewModel.validateSpecificationInputModelResult.observe(viewLifecycleOwner) { result ->
            val validationSuccess = result.first
            val validatedSelectedList = result.second

            if (validationSuccess) {
                val specificationList = specificationValueAdapter?.getDataSelectedList()
                viewModel.updateProductInputModelSpecifications(specificationList.orEmpty())
                submitInputData()
            } else {
                specificationValueAdapter?.setDataSelected(validatedSelectedList)
            }
        }
    }

    private fun observeErrorMessage() {
        viewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
            view?.apply {
                Toaster.build(this, errorMessage, type = Toaster.TYPE_ERROR).show()
            }
        })
    }

    private fun observeAnnotationCategoryData() {
        viewModel.annotationCategoryData.observe(viewLifecycleOwner, {
            val itemSelected = viewModel.getItemSelected(it)
            setupSpecificationAdapter(it, itemSelected)
            loaderSpecification?.gone()
            btnSpecification?.isEnabled = true
        })
    }

    private fun setupToolbarActions() {
        activity?.findViewById<HeaderUnify>(R.id.toolbar_specification)?.apply {
            headerTitle = getString(R.string.title_specification_activity)
            setNavigationOnClickListener {
                activity?.finish()
            }
            actionTextView?.setOnClickListener {
                viewModel.removeSpecification()
            }
            actionTextView?.text = getString(R.string.title_specification_activity_action)
            tvDeleteAll = actionTextView
            tvDeleteAll?.isEnabled = false
            tvDeleteAll?.visible()
        }
    }

    private fun setupSpecificationAdapter(annotationCategoryData: List<AnnotationCategoryData>, itemSelected: List<SpecificationInputModel>) {
        specificationValueAdapter = SpecificationValueAdapter(childFragmentManager)
        rvSpecification?.adapter = specificationValueAdapter
        setRecyclerViewToVertical(rvSpecification)
        specificationValueAdapter?.setData(annotationCategoryData, itemSelected)
        tvDeleteAll?.isEnabled = viewModel.getHasSpecification(itemSelected)
        specificationValueAdapter?.showOnSpecificationChanged {
            tvDeleteAll?.isEnabled = viewModel.getHasSpecification(it)
        }
    }

    private fun setupSubmitButton() {
        btnSpecification?.setOnClickListener {
            specificationValueAdapter?.apply {
                viewModel.validateSpecificationInputModel(getDataSelectedList())
            }
        }
    }

    private fun submitInputData() {
        viewModel.productInputModel.value?.apply {
            val cacheManagerId = arguments?.getString(EXTRA_CACHE_MANAGER_ID).orEmpty()
            SaveInstanceCacheManager(requireContext(), cacheManagerId).put(EXTRA_PRODUCT_INPUT_MODEL, this)

            val intent = Intent().putExtra(EXTRA_CACHE_MANAGER_ID, cacheManagerId)
            activity?.setResult(Activity.RESULT_OK, intent)
        }
        activity?.finish()
    }

    private fun setRecyclerViewToVertical(recyclerView: RecyclerView?) {
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}
