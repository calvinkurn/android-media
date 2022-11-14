package com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.common.util.TokofoodExt.addAndReturnImpressionListener
import com.tokopedia.tokofood.databinding.ItemTokofoodIconBinding
import com.tokopedia.tokofood.databinding.ItemTokofoodIconsBinding
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.domain.data.DynamicIcon
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class TokoFoodHomeIconsViewHolder(
    itemView: View,
    private val homeIconsListener: TokoFoodHomeIconsListener? = null,
    private val tokofoodScrollChangedListener: TokofoodScrollChangedListener?
): AbstractViewHolder<TokoFoodHomeIconsUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_icons

        private const val GRID_ITEM = 4
        private const val MAX_ICON_ITEM = 8
    }

    private val binding: ItemTokofoodIconsBinding? by viewBinding()
    private val adapter = TokoFoodIconAdapter()
    private var iconRecyclerView: RecyclerView? = null
    private var shimmeringLayout: View? = null

    override fun bind(element: TokoFoodHomeIconsUiModel) {
        setupTokoFoodIcon()
        when(element.state){
            TokoFoodLayoutState.SHOW -> showTokoFoodIcon(element)
            TokoFoodLayoutState.LOADING -> showLoadingLayout()
        }
    }

    private fun setupTokoFoodIcon(){
        iconRecyclerView = binding?.tokofoodIconRecyclerView
        shimmeringLayout = binding?.tokofoodIconShimmering?.root
    }

    private fun showLoadingLayout(){
        iconRecyclerView?.hide()
        shimmeringLayout?.show()
    }

    private fun showTokoFoodIcon(element: TokoFoodHomeIconsUiModel){
        iconRecyclerView?.show()
        shimmeringLayout?.hide()
        val icons = element.listIcons
        if (!icons.isNullOrEmpty()){
            adapter.submitList(element.listIcons, element.verticalPosition)
        }
        iconRecyclerView?.adapter = adapter
        iconRecyclerView?.layoutManager = GridLayoutManager(itemView.context, GRID_ITEM, GridLayoutManager.VERTICAL, false)
        setItemViewImpression(element)
    }

    private fun setItemViewImpression(element: TokoFoodHomeIconsUiModel) {
        tokofoodScrollChangedListener?.let { scrollChangedListener ->
            itemView.addAndReturnImpressionListener(element, scrollChangedListener) {
                element.listIcons?.let {
                    homeIconsListener?.onImpressHomeIcon(it.take(MAX_ICON_ITEM), verticalPosition = element.verticalPosition)
                }
            }
        }
    }

    internal inner class TokoFoodIconAdapter: RecyclerView.Adapter<TokoFoodIconViewHolder>() {
        private val iconList = mutableListOf<DynamicIcon>()
        private var verticalPosition : Int = -1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokoFoodIconViewHolder {
            val bindingIcon = ItemTokofoodIconBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return TokoFoodIconViewHolder(bindingIcon)
        }

        override fun onBindViewHolder(holder: TokoFoodIconViewHolder, position: Int) {
            iconList.getOrNull(position)?.let {
                holder.bind(it, verticalPosition)
            }
        }

        override fun getItemCount(): Int {
            return iconList.size
        }

        fun submitList(list: List<DynamicIcon>, verticalPosition: Int) {
            list?.let {
                iconList.clear()
                iconList.addAll(it.take(MAX_ICON_ITEM))
            }
            this.verticalPosition = verticalPosition
        }
    }

    internal inner class TokoFoodIconViewHolder(private val bindingIcon: ItemTokofoodIconBinding):
        RecyclerView.ViewHolder(bindingIcon.root){
        var imgIcon : ImageUnify? = null
        var tgIcon : Typography? = null

        fun bind(item: DynamicIcon, verticalPosition: Int){
            imgIcon = bindingIcon.imgIconTokofoodHome
            tgIcon = bindingIcon.tgIconTokofoodHome

            imgIcon?.loadImage(item.imageUrl)
            tgIcon?.text = item.name

            bindingIcon.root.setOnClickListener {
                homeIconsListener?.onClickHomeIcon(item.applinks, listOf(item), horizontalPosition = adapterPosition,
                    verticalPosition = verticalPosition)
            }
        }
    }

    interface TokoFoodHomeIconsListener {
        fun onClickHomeIcon(applink: String, data: List<DynamicIcon>, horizontalPosition: Int, verticalPosition: Int)
        fun onImpressHomeIcon(data: List<DynamicIcon>, verticalPosition: Int)
    }
}
