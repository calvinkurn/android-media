package com.tokopedia.buy_more_get_more.olp.presentation.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buy_more_get_more.databinding.BmgmBottomSheetTncBinding
import com.tokopedia.buy_more_get_more.olp.presentation.bottomsheet.adapter.TncAdapter
import com.tokopedia.buy_more_get_more.olp.utils.BundleConstant
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TncBottomSheet: BottomSheetUnify() {

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
        adapter = TncAdapter().apply {
            setDataList(tnc ?: return)
        }
    }

    fun show(fragment: Fragment) {
        setTitle("S&K Pembelian")
        show(fragment.childFragmentManager, "")
    }

}



