package com.tokopedia.dilayanitokopedia.home.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.dilayanitokopedia.databinding.DtItemAnchorTabsBinding
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.AnchorTabUiModel
import com.tokopedia.unifycomponents.CardUnify


class DtAnchorTabAdapter(private val listener: AnchorTabListener) :
    RecyclerView.Adapter<DtAnchorTabAdapter.DtAnchorTabViewHolder>() {

    var listMenu = mutableListOf<AnchorTabUiModel>()

    private var selectedMenu = 0

    private var selectedPos = RecyclerView.NO_POSITION
    private var isItemClicked = false

    interface AnchorTabListener {
        fun onMenuSelected(anchorTabUiModel: AnchorTabUiModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DtAnchorTabViewHolder {
        val binding = DtItemAnchorTabsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DtAnchorTabViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return listMenu.size
    }

    override fun onBindViewHolder(holder: DtAnchorTabViewHolder, position: Int) {
        holder.itemView.isClickable = true
        if (position == selectedMenu) {
            holder.bindData(listMenu[position], true)
        } else {
            holder.bindData(listMenu[position], false)
        }

    }

    fun updateList(data: List<AnchorTabUiModel>) {
        listMenu.addAll(data)
        notifyDataSetChanged()
    }

    fun selectMenu(selectedMenu: AnchorTabUiModel) {
        this.selectedMenu = listMenu.indexOf(selectedMenu)
        notifyDataSetChanged()
    }

    fun clearData() {
//        selectedPos = RecyclerView.NO_POSITION
        listMenu.clear()
        notifyDataSetChanged()
    }

    inner class DtAnchorTabViewHolder(
        private val binding: DtItemAnchorTabsBinding,
        private val listener: AnchorTabListener
    ) : RecyclerView.ViewHolder(binding.root) {
//        val assetMoreBtn = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_more_horiz)

        fun bindData(data: AnchorTabUiModel, isSelected: Boolean) {


            binding.anchorText.text = data.title



            cardSelection(isSelected)
            setListener(data)
        }

//        private fun setPrimary(peopleAddress: AnchorTabUiModel) {
//            if (peopleAddress.addressStatus == 2) {
//                binding.lblMainAddress.visible()
//                binding.btnSecondary.gone()
//            } else {
//                binding.lblMainAddress.gone()
//                binding.btnSecondary.visible()
//            }
//        }

        //        private fun setVisibility(peopleAddress: AnchorTabUiModel) {
//            if (peopleAddress.latitude.isNullOrEmpty() || peopleAddress.longitude.isNullOrEmpty() ||
//                peopleAddress.latitude == "0.0" || peopleAddress.longitude == "0.0"
//            ) {
//                val colorGrey = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
//                binding.imgLocationState.setImage(IconUnify.LOCATION_OFF, colorGrey, colorGrey)
//                binding.tvPinpointState.text = itemView.context.getString(R.string.no_pinpoint)
//            } else {
//                val colorGreen = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
//                binding.imgLocationState.setImage(IconUnify.LOCATION, colorGreen, colorGreen)
//                binding.tvPinpointState.text = itemView.context.getString(R.string.pinpoint)
//            }
//        }
//
        private fun setListener( anchorTabUiModel: AnchorTabUiModel) {
            binding.root.setOnClickListener {
                listener.onMenuSelected(anchorTabUiModel)
            }
        }

        private fun cardSelection(isSelected: Boolean) {
            if (isSelected) {
                binding.anchorCard.cardType = CardUnify.TYPE_BORDER_ACTIVE
            } else {
                binding.anchorCard.cardType = CardUnify.TYPE_SHADOW
            }
        }
    }
}
