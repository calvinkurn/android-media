package com.tokopedia.product_bundle.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.activity.ProductBundleActivity
import com.tokopedia.product_bundle.common.extension.setBackgroundToWhite
import com.tokopedia.totalamount.TotalAmount

class EntrypointFragment : TkpdBaseV4Fragment() {

    private var layoutShimmer: ViewGroup? = null
    private var layoutError: GlobalError? = null

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

    fun showShimmering() {
        layoutShimmer?.show()
        layoutError?.hide()
    }

    fun showError() {
        layoutShimmer?.hide()
        layoutError?.show()
    }

    fun showSuccess() {
        layoutShimmer?.hide()
        layoutError?.hide()
    }
}