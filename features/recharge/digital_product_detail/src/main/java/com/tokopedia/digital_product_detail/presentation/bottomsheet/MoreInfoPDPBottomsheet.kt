package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.digital_product_detail.data.model.data.TelcoFilterTagComponent
import com.tokopedia.digital_product_detail.databinding.BottomSheetMoreInfoBinding
import com.tokopedia.digital_product_detail.presentation.adapter.DigitalMoreInfoAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.lang.reflect.Type

class MoreInfoPDPBottomsheet: BottomSheetUnify() {

    private lateinit var listInfo: List<String>
    private lateinit var titleBottomsheet: String

    private var binding by autoClearedNullable<BottomSheetMoreInfoBinding>()

    fun setTitleBottomSheet(titleBottomsheet: String) {
        this.titleBottomsheet = titleBottomsheet
    }

    fun setListInfo(listInfo: List<String>) {
        this.listInfo = listInfo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            if (it.containsKey(SAVED_INSTANCE_MANAGER_ID)) {
                val manager = SaveInstanceCacheManager(
                    requireContext(),
                    it.getString(SAVED_INSTANCE_MANAGER_ID)
                )
                titleBottomsheet = manager.getString(SAVED_MORE_INFO_TITLE) ?: ""

                val type: Type = object : TypeToken<List<String>>() {}.type
                listInfo = manager.get(SAVED_MORE_INFO_DATA, type) ?: emptyList<String>()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        context?.run {
            val manager = SaveInstanceCacheManager(this, true).also {
                it.put(SAVED_MORE_INFO_DATA, listInfo)
                it.put(SAVED_MORE_INFO_TITLE, titleBottomsheet)
            }
            outState.putString(SAVED_INSTANCE_MANAGER_ID, manager.id)
        }
    }

    private fun initView(){
        isFullpage = false
        isDragable = false
        showCloseIcon = true
        clearContentPadding = true

        binding = BottomSheetMoreInfoBinding.inflate(LayoutInflater.from(context))
        binding?.run {
            if (::listInfo.isInitialized) {
                rvMoreInfo.run {
                    val adapterMoreInfo = DigitalMoreInfoAdapter()
                    val linearLayoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    layoutManager = linearLayoutManager
                    adapter = adapterMoreInfo
                    adapterMoreInfo.setListInfo(listInfo)
                }
            }
        }
        if (::titleBottomsheet.isInitialized) setTitle(titleBottomsheet)
        setChild(binding?.root)
    }

    companion object {
        private const val SAVED_INSTANCE_MANAGER_ID = "SAVED_INSTANCE_MANAGER_ID"
        private const val SAVED_MORE_INFO_DATA = "SAVED_MORE_INFO_DATA"
        private const val SAVED_MORE_INFO_TITLE = "SAVED_MORE_INFO_TITLE"

        fun getInstance(): MoreInfoPDPBottomsheet = MoreInfoPDPBottomsheet()
    }
}