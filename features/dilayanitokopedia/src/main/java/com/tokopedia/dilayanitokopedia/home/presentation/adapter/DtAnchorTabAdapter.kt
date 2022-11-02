package com.tokopedia.dilayanitokopedia.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.dilayanitokopedia.databinding.DtItemAnchorTabsBinding
import com.tokopedia.dilayanitokopedia.home.presentation.uimodel.AnchorTabUiModel


class DtAnchorTabAdapter(private val listener: ManageAddressItemAdapterListener) :
    RecyclerView.Adapter<DtAnchorTabAdapter.ManageAddressViewHolder>() {

    var addressList = mutableListOf<AnchorTabUiModel>()

    //    private var selectedPos = RecyclerView.NO_POSITION
    private var isItemClicked = false

    interface ManageAddressItemAdapterListener {
        fun onManageAddressEditClicked(peopleAddress: AnchorTabUiModel)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageAddressViewHolder {
        val binding = DtItemAnchorTabsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ManageAddressViewHolder(binding, listener)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: ManageAddressViewHolder, position: Int) {
        holder.itemView.isClickable = true
        holder.bindData(addressList[position])
    }

    fun addList(data: List<AnchorTabUiModel>) {
        addressList.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
//        selectedPos = RecyclerView.NO_POSITION
        addressList.clear()
        notifyDataSetChanged()
    }

    inner class ManageAddressViewHolder(
        private val binding: DtItemAnchorTabsBinding,
        private val listener: ManageAddressItemAdapterListener
    ) : RecyclerView.ViewHolder(binding.root) {
//        val assetMoreBtn = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_more_horiz)

        fun bindData(data: AnchorTabUiModel) {


            binding.anchorText.text = data.title
//                val addressStreet = data.street
//                val tokopediaNoteCondition = context.getString(R.string.tokopedia_note_delimeter)
//                setVisibility(data)
//                setPrimary(data)
//                binding.addressName.text = data.addressName
//                binding.receiverName.text = data.recipientName
//                binding.receiverPhone.text = data.recipientPhoneNumber
//                if (addressStreet.contains(tokopediaNoteCondition)) {
//                    val tokopediaNote = tokopediaNoteCondition + addressStreet.substringAfterLast(tokopediaNoteCondition)
//                    val newAddress = addressStreet.replace(tokopediaNote, "")
//                    binding.tokopediaNote.visible()
//                    binding.tokopediaNote.text = tokopediaNote
//                    binding.addressDetail.text = newAddress
//                } else {
//                    binding.tokopediaNote.gone()
//                    binding.addressDetail.text = data.street
//                }
//                val bitmap = assetMoreBtn?.toBitmap()
//                val d: Drawable =
//                    BitmapDrawable(resources, bitmap?.let { Bitmap.createScaledBitmap(it, 80.toDp(), 80.toDp(), true) })
//                binding.btnSecondary.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null)
//
//                val cardSelected: Boolean
//                if (data.isStateChosenAddress && !isItemClicked) {
//                    cardSelected = true
//                    selectedPos = layoutPosition
//                } else {
//                    cardSelected = selectedPos == layoutPosition
//                }
//                binding.cardAddress.hasCheckIcon = cardSelected
//                if (cardSelected) {
//                    binding.cardAddress.cardType = CardUnify.TYPE_BORDER_ACTIVE
//                } else {
//                    binding.cardAddress.cardType = CardUnify.TYPE_BORDER
//                }
//                setListener(itemView, data)
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
//        private fun setListener(itemView: View, peopleAddress: AnchorTabUiModel) {
//            binding.btnPrimary.setOnClickListener {
//                listener.onManageAddressEditClicked(peopleAddress)
//            }
//            binding.btnSecondary.setOnClickListener {
//                listener.onManageAddressLainnyaClicked(peopleAddress)
//            }
//            binding.cardAddress.setOnClickListener {
//                isItemClicked = true
//                notifyItemChanged(selectedPos)
//                selectedPos = layoutPosition
//                notifyItemChanged(layoutPosition)
//                listener.onAddressItemSelected(peopleAddress)
//            }
//        }
    }
}
