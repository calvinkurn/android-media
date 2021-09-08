package com.tokopedia.product_bundle.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.activity.ProductBundleActivity
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.BUNDLE_EMPTY_IMAGE_URL
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.PAGE_SOURCE_CART
import com.tokopedia.product_bundle.common.extension.setBackgroundToWhite
import com.tokopedia.totalamount.TotalAmount

class EntrypointFragment : TkpdBaseV4Fragment() {

    private var layoutShimmer: ViewGroup? = null
    private var layoutError: GlobalError? = null
    private var pageSource = ""
    private var productId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_entrypoint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setBackgroundToWhite()
        setupShimmer(view)
        setupGlobalError(view)
    }

    override fun getScreenName() = null

    private fun setupShimmer(view: View) {
        val totalAmountShimmer: TotalAmount? = view.findViewById(R.id.total_amount)
        totalAmountShimmer?.isTotalAmountLoading = true
        layoutShimmer = view.findViewById(R.id.layout_shimmer)
    }

    private fun setupGlobalError(view: View) {
        layoutError = view.findViewById(R.id.layout_error)
        layoutError?.apply {
            errorAction.setOnClickListener {
                refreshPage()
            }
            hide()
        }
    }

    private fun refreshPage() {
        val productBundleActivity = requireActivity() as ProductBundleActivity
        productBundleActivity.refreshPage()
    }

    private fun GlobalError.showError() {
        setType(GlobalError.SERVER_ERROR)
    }

    private fun GlobalError.showEmpty() {
        if (pageSource == PAGE_SOURCE_CART) {
            setType(GlobalError.NO_CONNECTION)
        } else {
            setType(GlobalError.PAGE_FULL)
        }
        errorIllustration.loadImageWithoutPlaceholder(BUNDLE_EMPTY_IMAGE_URL)
        errorTitle.text = getString(R.string.single_bundle_error_bundle)
        errorDescription.text = getString(R.string.single_bundle_error_bundle_desc)
        errorAction.text = getString(R.string.action_back_to_pdp)
        errorSecondaryAction.text = getString(R.string.action_back_to_cart)
        errorAction.setOnClickListener {
            if (pageSource == PAGE_SOURCE_CART)
                RouteManager.route(context, ApplinkConst.PRODUCT_INFO, productId)
            activity?.finish()
        }
        errorSecondaryAction.setOnClickListener {
            activity?.finish()
        }
    }

    fun setProductId(productId: String) {
        this.productId = productId
    }

    fun setPageSource(source: String) {
        pageSource = source
    }

    fun showShimmering() {
        layoutShimmer?.show()
        layoutError?.hide()
    }

    fun showError() {
        layoutShimmer?.hide()
        layoutError?.show()
        layoutError?.showError()
    }

    fun showEmpty() {
        layoutShimmer?.hide()
        layoutError?.show()
        layoutError?.showEmpty()
    }
}