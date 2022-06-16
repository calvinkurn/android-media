package com.tokopedia.sellerhome.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerhome.R
import com.tokopedia.shop.common.constant.admin_roles.AdminPermissionUrl
import com.tokopedia.unifycomponents.BottomSheetUnify

class AdminRestrictionBottomSheet: BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context,
                           articleUrl: String): AdminRestrictionBottomSheet =
            AdminRestrictionBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(KEY_ARTICLE_URL, articleUrl)
                }
                val view = View.inflate(
                    context,
                    R.layout.bottom_sheet_sah_admin_restriction,
                    null
                )
                setChild(view)
            }

        private const val TAG = "AdminRestrictionBottomSheet"

        private const val KEY_ARTICLE_URL = "article_url"
    }

    private val articleUrl by lazy {
        arguments?.getString(KEY_ARTICLE_URL).orEmpty()
    }

    private var adminRestrictionEmptyState: EmptyStateUnify? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
        super.onViewCreated(view, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupView(view: View) {
        adminRestrictionEmptyState = view.findViewById(R.id.empty_state_sah_admin_restriction)
        adminRestrictionEmptyState?.run {
            setImageUrl(AdminPermissionUrl.ERROR_ILLUSTRATION)
            setPrimaryCTAClickListener {
                this@AdminRestrictionBottomSheet.dismiss()
            }
        }
        if (articleUrl.isNotBlank()) {
            setDescriptionArticle(articleUrl)
        }
    }

    private fun setDescriptionArticle(articleUrl: String) {
        adminRestrictionEmptyState?.emptyStateDescriptionID?.run {
            text = getString(R.string.sah_admin_restriction_article_desc).parseAsHtml()
            setOnClickListener {
                goToArticleUrl(articleUrl)
            }
        }
    }

    private fun goToArticleUrl(articleUrl: String) {
        RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, articleUrl)
    }

}