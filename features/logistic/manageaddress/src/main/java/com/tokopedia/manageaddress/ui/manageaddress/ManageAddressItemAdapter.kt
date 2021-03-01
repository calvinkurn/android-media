package com.tokopedia.manageaddress.ui.manageaddress

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.manageaddress.R
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_manage_people_address.view.*


class ManageAddressItemAdapter(private val listener: ManageAddressItemAdapterListener) : RecyclerView.Adapter<ManageAddressItemAdapter.ManageAddressViewHolder>() {

    var addressList = mutableListOf<RecipientAddressModel>()
    var token: Token? = null
    private var selectedPos = RecyclerView.NO_POSITION

    interface ManageAddressItemAdapterListener {
        fun onManageAddressEditClicked(peopleAddress: RecipientAddressModel)
        fun onManageAddressLainnyaClicked(peopleAddress: RecipientAddressModel)
        fun onAddressItemSelected(peopleAddress: RecipientAddressModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageAddressViewHolder {
        return ManageAddressViewHolder(parent.inflateLayout(R.layout.item_manage_people_address), listener)
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: ManageAddressViewHolder, position: Int) {
        holder.itemView.isClickable = true
        holder.bindData(addressList[position])
    }

    fun addList(data: List<RecipientAddressModel>) {
        addressList.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        addressList.clear()
        notifyDataSetChanged()
    }

    inner class ManageAddressViewHolder(itemView: View, private val listener: ManageAddressItemAdapterListener) : RecyclerView.ViewHolder(itemView) {
        val pinpointText = itemView.findViewById<Typography>(R.id.tv_pinpoint_state)
        val imageLocation = itemView.findViewById<ImageView>(R.id.img_location_state)
        val btnPrimary = itemView.findViewById<UnifyButton>(R.id.btn_primary)
        val btnSecondary = itemView.findViewById<UnifyButton>(R.id.btn_secondary)
        val cardAddress = itemView.findViewById<CardUnify>(R.id.card_address)
        val assetMoreBtn = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_more_horiz)

        @SuppressLint("SetTextI18n")
        fun bindData(data: RecipientAddressModel) {
            with(itemView) {
                val addressStreet = data.street
                val postalCode = data.postalCode
                val tokopediaNoteCondition = context.getString(R.string.tokopedia_note_delimeter)
                setVisibility(data)
                setPrimary(data)
                address_name.text = data.addressName
                receiver_name.text = data.recipientName
                receiver_phone.text = data.recipientPhoneNumber
                if (addressStreet.contains(tokopediaNoteCondition)) {
                    val tokopediaNote = tokopediaNoteCondition + addressStreet.substringAfterLast(tokopediaNoteCondition)
                    val newAddress = addressStreet.replace(tokopediaNote, "")
                    tokopedia_note.visible()
                    tokopedia_note.text = tokopediaNote
                    address_detail.text = if (addressStreet.contains(postalCode)) newAddress else newAddress + ", " + data.postalCode
                } else {
                    tokopedia_note.gone()
                    address_detail.text = data.street + ", " + data.postalCode
                }
                val bitmap = (assetMoreBtn as VectorDrawable).toBitmap()
                val d: Drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 80.toDp(), 80.toDp(), true))
                btnSecondary.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null)

                val cardSelected = selectedPos == layoutPosition
                cardAddress.hasCheckIcon = cardSelected
                if (cardSelected) {
                    cardAddress.cardType = CardUnify.TYPE_BORDER_ACTIVE
                } else {
                    cardAddress.cardType = CardUnify.TYPE_BORDER
                }

                setListener(itemView, data)
            }
        }

        private fun setPrimary(peopleAddress: RecipientAddressModel) {
            if (peopleAddress.addressStatus == 2) {
                itemView.lbl_main_address.visible()
                itemView.btn_secondary.gone()
            } else {
                itemView.lbl_main_address.gone()
                itemView.btn_secondary.visible()
            }
        }

        private fun setVisibility(peopleAddress: RecipientAddressModel) {
            if(peopleAddress.latitude.isNullOrEmpty()|| peopleAddress.longitude.isNullOrEmpty()) {
                val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_no_pinpoint)
                imageLocation.setImageDrawable(icon)
                pinpointText.text = itemView.context.getString(R.string.no_pinpoint)
                pinpointText.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            } else {
                val icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_pinpoint_green)
                imageLocation.setImageDrawable(icon)
                pinpointText.text = itemView.context.getString(R.string.pinpoint)
                pinpointText.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            }
        }

        private fun setListener(itemView: View, peopleAddress: RecipientAddressModel) {
            btnPrimary.setOnClickListener  {
                listener.onManageAddressEditClicked(peopleAddress)
            }
            btnSecondary.setOnClickListener {
                listener.onManageAddressLainnyaClicked(peopleAddress)
            }
            cardAddress.setOnClickListener {
                notifyItemChanged(selectedPos)
                selectedPos = layoutPosition
                notifyItemChanged(layoutPosition)
                listener.onAddressItemSelected(peopleAddress)
            }
        }
    }
}