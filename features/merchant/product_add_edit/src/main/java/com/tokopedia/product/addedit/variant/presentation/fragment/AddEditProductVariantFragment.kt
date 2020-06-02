package com.tokopedia.product.addedit.variant.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXTRA_CACHE_MANAGER_ID
import com.tokopedia.product.addedit.common.util.HorizontalItemDecoration
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_INPUT_MODEL
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.di.AddEditProductVariantComponent
import com.tokopedia.product.addedit.variant.presentation.activity.AddEditProductVariantDetailActivity
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantPhotoAdapter
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantTypeAdapter
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantValueAdapter
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MAX_SELECTED_VARIANT_TYPE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.REQUEST_CODE_VARIANT_DETAIL
import com.tokopedia.product.addedit.variant.presentation.viewmodel.AddEditProductVariantViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.add_edit_product_variant_photo_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_type_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_value_level1_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_value_level2_layout.*
import kotlinx.android.synthetic.main.fragment_add_edit_product_variant.*
import javax.inject.Inject

class AddEditProductVariantFragment : BaseDaggerFragment(), VariantTypeAdapter.OnVariantTypeClickListener {

    companion object {
        fun createInstance(cacheManagerId: String): Fragment {
            return AddEditProductVariantFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_CACHE_MANAGER_ID, cacheManagerId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModel: AddEditProductVariantViewModel
    private var variantTypeAdapter: VariantTypeAdapter? = null

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(AddEditProductVariantComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
        val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

        cacheManagerId?.run {
            viewModel.productInputModel.value = saveInstanceCacheManager.get(EXTRA_PRODUCT_INPUT_MODEL,
                    ProductInputModel::class.java) ?: ProductInputModel()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_variant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        variantTypeAdapter = VariantTypeAdapter(this)
        val variantValueAdapter = VariantValueAdapter()
        val variantPhotoAdapterAdapter = VariantPhotoAdapter()
        recyclerViewVariantType.adapter = variantTypeAdapter
        recyclerViewVariantValueLevel1.adapter = variantValueAdapter
        recyclerViewVariantValueLevel2.adapter = variantValueAdapter
        recyclerViewVariantPhoto.adapter = variantPhotoAdapterAdapter
        setRecyclerViewToFlex(recyclerViewVariantType)
        setRecyclerViewToFlex(recyclerViewVariantValueLevel1)
        setRecyclerViewToFlex(recyclerViewVariantValueLevel2)
        setRecyclerViewToHorizontal(recyclerViewVariantPhoto)

        observeGetCategoryVariantCombinationResult()
        observeProductInputModel()
        viewModel.getCategoryVariantCombination("916")

        buttonSave.setOnClickListener {
            startAddEditProductVariantDetailActivity()
        }

        Handler().postDelayed({
            val variants: List<String> = listOf("ukuran yang menentukan semua yang terukur", "warna", "rasya", "dsdsdsf", "asdasdas dasd", "sadsdsdsdasda")
            variantValueAdapter.setData(variants)
            variantPhotoAdapterAdapter.setData(variants)
        }, 1000)
    }

    override fun onVariantTypeClicked(selectedVariantDetails: List<VariantDetail>) {
        // TODO implement selectedVariantDetails to variant values sections
        selectedVariantDetails.forEach {
            Log.e("--", it.name)
        }
    }

    private fun observeGetCategoryVariantCombinationResult() {
        viewModel.getCategoryVariantCombinationResult.observe(this, Observer { result ->
            when (result) {
                is Success -> {
                    val variantDetails =
                            result.data.getCategoryVariantCombination.data.variantDetails
                    variantTypeAdapter?.setData(variantDetails)
                    variantTypeAdapter?.setMaxSelectedItems(MAX_SELECTED_VARIANT_TYPE)
                }
                is Fail -> {
                    context?.let {
                        showGetCategoryVariantCombinationErrorToast(
                                ErrorHandler.getErrorMessage(it, result.throwable))
                    }
                }
            }
        })
    }

    private fun observeProductInputModel() {
        viewModel.productInputModel.observe(this, Observer { productInputModel ->
            // TODO implement productInputModel to UI
        })
    }

    private fun showGetCategoryVariantCombinationErrorToast(errorMessage: String) {
        view?.let {
            Toaster.make(it, errorMessage,
                    type = Toaster.TYPE_ERROR,
                    actionText = getString(R.string.title_try_again),
                    duration = Snackbar.LENGTH_INDEFINITE,
                    clickListener = View.OnClickListener {
                        viewModel.getCategoryVariantCombination("916")
                    })
        }
    }

    private fun setRecyclerViewToFlex(recyclerView: RecyclerView) {
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.alignItems = AlignItems.FLEX_START
        recyclerView.apply {
            layoutManager = flexboxLayoutManager
            addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_lvl3)))
        }
    }

    private fun setRecyclerViewToHorizontal(recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_lvl3)))
        }
    }

    private fun startAddEditProductVariantDetailActivity() {
        context?.run {
            val cacheManager = SaveInstanceCacheManager(this, true).apply {
                put(EXTRA_PRODUCT_INPUT_MODEL, viewModel.productInputModel.value)
            }
            val intent = Intent(this,
                    AddEditProductVariantDetailActivity::class.java).apply {
                putExtra(EXTRA_CACHE_MANAGER_ID, cacheManager.id)
            }
            startActivityForResult(intent, REQUEST_CODE_VARIANT_DETAIL)
        }
    }

    fun onBackPressed() {
        activity?.finish()
    }

}
