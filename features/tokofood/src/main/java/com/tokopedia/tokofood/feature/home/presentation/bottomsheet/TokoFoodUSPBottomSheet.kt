package com.tokopedia.tokofood.feature.home.presentation.bottomsheet

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.databinding.BottomsheetTokofoodUspBinding
import com.tokopedia.tokofood.feature.home.domain.data.USPResponse
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodHomeTextMapper
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodUSPAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify

class TokoFoodUSPBottomSheet: BottomSheetUnify() {

    private var viewBinding: BottomsheetTokofoodUspBinding? = null
    private var uspData: USPResponse? = null
    private var uspTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            if (it.containsKey(SAVED_INSTANCE_MANAGER_ID)) {
                val manager = SaveInstanceCacheManager(
                    requireContext(),
                    it.getString(SAVED_INSTANCE_MANAGER_ID)
                )

                uspData = manager.get(SAVED_USP_DATA, USPResponse::class.java) ?: USPResponse()
                uspTitle = manager.getString(SAVED_TITLE_DATA, "")
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderUSP()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        context?.run {
            val manager = SaveInstanceCacheManager(this, true).also {
                it.put(SAVED_USP_DATA, uspData)
                it.put(SAVED_TITLE_DATA, uspTitle)
            }
            outState.putString(SAVED_INSTANCE_MANAGER_ID, manager.id)
        }
    }

    fun setUSP(uspData: USPResponse, uspTitle: String) {
        this.uspData = uspData
        this.uspTitle = uspTitle
    }

    private fun initView(): BottomsheetTokofoodUspBinding{
        val viewBinding = BottomsheetTokofoodUspBinding.inflate(LayoutInflater.from(context))
        this.viewBinding = viewBinding
        initBottomSheet(viewBinding)
        return viewBinding
    }

    private fun initBottomSheet(viewBinding: BottomsheetTokofoodUspBinding) {
        uspTitle?.let { setTitle(it) }
        clearContentPadding = true
        showCloseIcon = true
        isDragable = false
        setChild(viewBinding.root)
    }

    private fun renderUSP(){
        viewBinding?.apply {
            tgUsp.text = TokoFoodHomeTextMapper.removeUnderlineFromLink(uspData?.footer ?: "")
            tgUsp.movementMethod = LinkMovementMethod.getInstance()
            imgUsp.loadImage(uspData?.imageUrl)

            uspData?.list?.let {
                val adapter = TokoFoodUSPAdapter(it)
                rvUsp.layoutManager = LinearLayoutManager(context)
                rvUsp.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        private const val SAVED_INSTANCE_MANAGER_ID = "SAVED_INSTANCE_MANAGER_ID"
        private const val SAVED_USP_DATA= "SAVED_USP_DATA"
        private const val SAVED_TITLE_DATA = "SAVED_TITLE_DATA"

        fun getInstance(): TokoFoodUSPBottomSheet = TokoFoodUSPBottomSheet()
    }
}