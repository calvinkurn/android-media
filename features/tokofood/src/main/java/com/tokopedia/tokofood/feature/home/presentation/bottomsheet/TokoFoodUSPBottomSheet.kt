package com.tokopedia.tokofood.feature.home.presentation.bottomsheet

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
        fun getInstance(): TokoFoodUSPBottomSheet = TokoFoodUSPBottomSheet()
    }
}