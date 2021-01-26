package com.tokopedia.shop.pageheader.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.shop.pageheader.presentation.adapter.ShopRequestUnmoderateBottomSheetAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

/**
 * author by Rafli Syam on 22/01/2021
 */
class ShopRequestUnmoderateBottomSheet : BottomSheetUnify() {

    companion object {
        @LayoutRes
        private val LAYOUT =  R.layout.shop_request_unmoderate_bottomsheet
        private val TAG = ShopRequestUnmoderateBottomSheet::class.java.simpleName

        fun createInstance() : ShopRequestUnmoderateBottomSheet = ShopRequestUnmoderateBottomSheet()
    }

    private var rvRequestOptions: RecyclerView? = null
    private var buttonSendUnmoderateRequest: UnifyButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupBottomSheetChildView(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initListener()
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            show(it, TAG)
        }
    }

    private fun setupBottomSheetChildView(inflater: LayoutInflater, container: ViewGroup?) {
        inflater.inflate(LAYOUT, container).apply {
            rvRequestOptions = findViewById(R.id.rv_req_unmoderate_options)
            buttonSendUnmoderateRequest = findViewById(R.id.btn_send_req_unmoderate)
            setTitle(getString(R.string.shop_page_header_request_unmoderate_bottomsheet_title))
            setChild(this)
            setCloseClickListener {
                dismiss()
            }
        }
    }

    private fun initRecyclerView() {
        rvRequestOptions?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ShopRequestUnmoderateBottomSheetAdapter(context)
        }
    }

    private fun initListener() {
        buttonSendUnmoderateRequest?.setOnClickListener {
            Toast.makeText(context, "Test from bottomsheet", Toast.LENGTH_LONG).show()
        }
    }

}