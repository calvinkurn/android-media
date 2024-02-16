package com.tokopedia.loginregister.forbidden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginregister.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.url.TokopediaUrl

/**
 * Created by meyta on 2/22/18.
 */
class ForbiddenFragment : TkpdBaseV4Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_forbidden_session, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = view.findViewById<TextView>(R.id.tv_title_forbidden)
        val desc = view.findViewById<TextView>(R.id.tv_message_forbidden)
        val btnRetry = view.findViewById<UnifyButton>(R.id.btn_retry_forbidden)
        title.text = MethodChecker.fromHtml(getString(R.string.forbidden_title))
        desc.text = MethodChecker.fromHtml(getString(R.string.forbidden_msg))
        desc.setOnClickListener { v: View? ->
            RouteManager.route(
                activity,
                ApplinkConstInternalGlobal.WEBVIEW,
                URL
            )
        }
        btnRetry.setOnClickListener { requireActivity().finish() }
    }

    override fun getScreenName(): String {
        return FORBIDDEN_PAGE
    }

    companion object {
        private val URL = TokopediaUrl.getInstance().WEB + "terms?lang=id"
        private const val FORBIDDEN_PAGE = "Forbidden Page"
        fun createInstance(): ForbiddenFragment {
            val fragment = ForbiddenFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }
}
