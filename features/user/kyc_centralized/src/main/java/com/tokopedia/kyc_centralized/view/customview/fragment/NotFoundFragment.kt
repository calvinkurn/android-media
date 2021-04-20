package com.tokopedia.kyc_centralized.view.customview.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.user_identification_common.R

class NotFoundFragment : Fragment() {

    private lateinit var globalError: GlobalError

    companion object {
        fun createInstance(): Fragment {
            val fragment = NotFoundFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_not_found, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        globalError = view.findViewById(R.id.user_identification_global_error)
        globalError.setActionClickListener {
            RouteManager.route(context, ApplinkConst.HOME)
        }
    }
}
