package com.tokopedia.buy_more_get_more.olp.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.buy_more_get_more.databinding.BmgmBottomSheetTncBinding
import com.tokopedia.buy_more_get_more.olp.presentation.bottomsheet.adapter.TncAdapter
import com.tokopedia.buy_more_get_more.olp.presentation.bottomsheet.listener.TncListener
import com.tokopedia.buy_more_get_more.olp.utils.constant.BundleConstant
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import kotlin.collections.ArrayList

class TncBottomSheet : BottomSheetUnify(), TncListener {

    companion object {
        @JvmStatic
        fun newInstance(tnc: List<String>): TncBottomSheet {
            return TncBottomSheet().apply {
                arguments = Bundle().apply {
                    putStringArrayList(BundleConstant.BUNDLE_TNC, ArrayList(tnc))
                }
            }
        }
    }

    private var binding by autoClearedNullable<BmgmBottomSheetTncBinding>()
    private val tnc by lazy { arguments?.getStringArrayList(BundleConstant.BUNDLE_TNC) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            rvTnc.setupTnc()
        }
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = BmgmBottomSheetTncBinding.inflate(inflater, container, false)
        isKeyboardOverlap = false
        clearContentPadding = true
        setChild(binding?.root)
    }

    private fun RecyclerView.setupTnc() {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = TncAdapter(this@TncBottomSheet).apply {
            setDataList(tnc ?: return)
        }
    }

    fun show(fragment: Fragment) {
        setTitle("S&K Pembelian")
        show(fragment.childFragmentManager, "")
    }

    override fun onClickedTncUrl(url: String) {
        RouteManager.route(
            context,
            String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, url)
        )
    }
}
