package com.tokopedia.product.manage.item.main.draft.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkpd.library.ui.utilities.TkpdProgressDialog
import com.tkpd.library.utils.CommonUtils
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.common.util.AddEditPageType
import com.tokopedia.product.manage.item.common.util.ProductStatus
import com.tokopedia.product.manage.item.main.base.data.model.ProductViewModel
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment
import com.tokopedia.product.manage.item.main.draft.di.DaggerProductDraftComponent
import com.tokopedia.product.manage.item.main.draft.di.ProductDraftModule
import com.tokopedia.product.manage.item.main.draft.view.listener.ProductDraftView
import com.tokopedia.product.manage.item.main.draft.view.presenter.ProductDraftPresenterImpl
import com.tokopedia.product.manage.item.utils.convertToProductAddViewModel

open class ProductDraftEditFragment : BaseProductAddEditFragment<ProductDraftPresenterImpl, ProductDraftView>(), ProductDraftView{

    override var addEditPageType: AddEditPageType = AddEditPageType.DRAFT_EDIT
    override var statusUpload = ProductStatus.EDIT
    private var tkpdProgressDialog: TkpdProgressDialog? = null
    private var draftProductId: Long = 0
    private var cacheManagerId: String = ""


    override fun initInjector() {
        DaggerProductDraftComponent
                .builder()
                .productComponent(getComponent(ProductComponent::class.java))
                .productDraftModule(ProductDraftModule())
                .build()
                .inject(this)
    }

    override fun onSuccessLoadProduct(model: ProductViewModel?) {
        hideLoading()
        loadProduct(model)
    }

    override fun onErrorLoadProduct(throwable: Throwable?) {
        hideLoading()
        CommonUtils.UniversalToast(activity, getString(R.string.product_draft_error_cannot_load_draft))
        activity?.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        draftProductId = arguments?.getLong(DRAFT_PRODUCT_ID)?:0L
        cacheManagerId = arguments?.getString(CACHE_MANAGER_ID, "")?:""
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        if (savedInstanceState == null) {
            fetchInputData()
        }
        return view
    }

    private fun fetchInputData() {
        if (draftProductId >= 0) {
            showLoading()
            presenter.fetchDraftData(draftProductId)
        } else if (cacheManagerId.isNotEmpty()) {
            context?.let {
                val cacheManager = SaveInstanceCacheManager(it, cacheManagerId)
                val productViewModel = cacheManager.get<ProductViewModel>(PRODUCT_VIEW_MODEL, ProductViewModel::class.java)
                loadProduct(productViewModel)
            }
        }
    }

    protected fun showLoading() {
        if (tkpdProgressDialog == null) {
            tkpdProgressDialog = TkpdProgressDialog(activity,
                    TkpdProgressDialog.NORMAL_PROGRESS, getString(R.string.edit_product))
        }
        tkpdProgressDialog!!.showDialog()
    }

    protected fun hideLoading() {
        if (tkpdProgressDialog != null) {
            tkpdProgressDialog!!.dismiss()
        }
    }

    private fun loadProduct(model: ProductViewModel?){
        if(model?.productCategory != null){
            presenter.fetchProductVariantByCat(model.productCategory.categoryId)
        }
        currentProductAddViewModel = model?.convertToProductAddViewModel(isEditStatus())
        populateView(currentProductAddViewModel)
    }

    override fun getProductDraftId(): Long {
        return draftProductId
    }

    companion object {
        val DRAFT_PRODUCT_ID = "draft_product_id"
        val CACHE_MANAGER_ID = "cache_manager_id"

        fun createInstance(draftProductId: Long, cacheManagerId: String): Fragment {
            val fragment = ProductDraftEditFragment()
            val args = Bundle()
            args.putLong(DRAFT_PRODUCT_ID, draftProductId)
            args.putString(CACHE_MANAGER_ID, cacheManagerId)
            fragment.arguments = args
            return fragment
        }
    }

}