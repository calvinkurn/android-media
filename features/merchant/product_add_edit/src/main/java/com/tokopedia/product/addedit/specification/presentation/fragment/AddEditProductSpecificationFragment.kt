package com.tokopedia.product.addedit.specification.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXTRA_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.common.util.HorizontalItemDecoration
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.specification.di.DaggerAddEditProductSpecificationComponent
import com.tokopedia.product.addedit.specification.domain.model.AnnotationCategoryData
import com.tokopedia.product.addedit.specification.presentation.adapter.SpecificationValueAdapter
import com.tokopedia.product.addedit.specification.presentation.viewmodel.AddEditProductSpecificationViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_add_edit_product_specification.*
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
    private var tvDeleteAll: TextView? = null

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

        // setup UI
        setupToolbarActions()

        // setup observers
        observeProductInputModel()
        observeAnnotationCategoryData()
        observeErrorMessage()
    }

    private fun setupSpecificationAdapter(annotationCategoryData: List<AnnotationCategoryData>) {
        val adapter = SpecificationValueAdapter(fragmentManager)
        rvSpecification.adapter = adapter
        setRecyclerViewToVertical(rvSpecification)
        adapter.setData(annotationCategoryData)
    }

    private fun observeProductInputModel() {
        viewModel.productInputModel.observe(viewLifecycleOwner, Observer {
            viewModel.getSpecifications(it.detailInputModel.categoryId)
        })
    }

    private fun observeErrorMessage() {
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            view?.apply {
                Toaster.build(this, errorMessage, type = Toaster.TYPE_ERROR).show()
            }
        })
    }

    private fun observeAnnotationCategoryData() {
        viewModel.annotationCategoryData.observe(viewLifecycleOwner, Observer {
            setupSpecificationAdapter(it)
        })
    }

    private fun setRecyclerViewToVertical(recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)))
        }
    }

    private fun setupToolbarActions() {
        activity?.findViewById<HeaderUnify>(R.id.toolbar_specification)?.apply {
            headerTitle = getString(R.string.title_specification_activity)
            setNavigationOnClickListener {
                activity?.finish()
            }
            actionTextView?.setOnClickListener {
                //showRemoveSpecificationDialog()
            }
            actionTextView?.text = getString(R.string.title_specification_activity_action)
            tvDeleteAll = actionTextView
            tvDeleteAll?.isEnabled = false
        }
    }
}
