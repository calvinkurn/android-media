package com.tokopedia.manageaddress.ui.manageaddress

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.databinding.ItemManagePeopleAddressBinding
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.toDp

class ManageAddressItemAdapter : RecyclerView.Adapter<ManageAddressItemAdapter.ManageAddressViewHolder>() {

    var addressList = mutableListOf<RecipientAddressModel>()
    var token: Token? = null
    private var selectedPos = RecyclerView.NO_POSITION
    private var isItemClicked = false
    private var mainAddressListener: MainAddressItemAdapterListener? = null
    private var isShareAddress = false
    private var fromFriendAddressListener: FromFriendAddressItemAdapterListener? = null

    fun setMainAddressListener(isShareAddress: Boolean, listener: MainAddressItemAdapterListener) {
        this.isShareAddress = isShareAddress
        this.mainAddressListener = listener
    }

    fun setFromFriendAddressListener(listener: FromFriendAddressItemAdapterListener) {
        fromFriendAddressListener = listener
    }

    interface MainAddressItemAdapterListener {
        fun onManageAddressEditClicked(peopleAddress: RecipientAddressModel)
        fun onManageAddressLainnyaClicked(peopleAddress: RecipientAddressModel)
        fun onAddressItemSelected(peopleAddress: RecipientAddressModel)
        fun onShareAddressClicked(peopleAddress: RecipientAddressModel)
    }

    interface FromFriendAddressItemAdapterListener {
        fun onCheckedChangeListener(index: Int, isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageAddressViewHolder {
        val binding = ItemManagePeopleAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ManageAddressViewHolder(binding, mainAddressListener, fromFriendAddressListener)
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
        refreshAdapter()
    }

    fun initAddressList(data: MutableList<RecipientAddressModel>) {
        addressList = data
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshAdapter() {
        notifyDataSetChanged()
    }

    fun clearData() {
        selectedPos = RecyclerView.NO_POSITION
        addressList.clear()
        refreshAdapter()
    }

    inner class ManageAddressViewHolder(
        private val binding: ItemManagePeopleAddressBinding,
        private val manageAddressListener: MainAddressItemAdapterListener? = null,
        private val addressSharedListener: FromFriendAddressItemAdapterListener? = null,
    ) : RecyclerView.ViewHolder(binding.root) {
        val assetMoreBtn = AppCompatResources.getDrawable(itemView.context, R.drawable.ic_more_horiz)

        @SuppressLint("SetTextI18n")
        fun bindData(data: RecipientAddressModel) {
            with(itemView) {
                val addressStreet = data.street
                val tokopediaNoteCondition = context.getString(R.string.tokopedia_note_delimeter)
                setVisibility(data)
                switchActionView(data)
                binding.addressName.text = data.addressName
                binding.receiverName.text = data.recipientName
                binding.receiverPhone.text = getShownPhoneNumber(data.recipientPhoneNumber)
                if (addressStreet.contains(tokopediaNoteCondition)) {
                    val tokopediaNote = tokopediaNoteCondition + addressStreet.substringAfterLast(tokopediaNoteCondition)
                    val newAddress = addressStreet.replace(tokopediaNote, "")
                    binding.tokopediaNote.visible()
                    binding.tokopediaNote.text = tokopediaNote
                    binding.addressDetail.text = getShownAddress(newAddress)
                } else {
                    binding.tokopediaNote.gone()
                    binding.addressDetail.text = getShownAddress(data.street)
                }
                val bitmap = assetMoreBtn?.toBitmap()
                val d: Drawable = BitmapDrawable(resources, bitmap?.let { Bitmap.createScaledBitmap(it, 80.toDp(), 80.toDp(), true) })
                binding.btnSecondary.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null)

                val cardSelected: Boolean
                if (data.isStateChosenAddress && !isItemClicked && isMainAddressView()) {
                    cardSelected = true
                    selectedPos = layoutPosition
                } else {
                    cardSelected = selectedPos == layoutPosition
                }
                binding.cardAddress.hasCheckIcon = cardSelected && isShareAddress.not()
                if (cardSelected) {
                    binding.cardAddress.cardType = CardUnify.TYPE_BORDER_ACTIVE
                } else {
                    binding.cardAddress.cardType = CardUnify.TYPE_BORDER
                }
                setupCheckedSharedAddress(data)
                setListener(data)
            }
        }

        private fun isMainAddressView() : Boolean {
            return manageAddressListener != null && isShareAddress.not()
        }

        private fun setupCheckedSharedAddress(data: RecipientAddressModel) {
            if (addressSharedListener != null) {
                binding.cbSelectAddress.isChecked = data.isSelected
            }
        }

        private fun switchActionView(data: RecipientAddressModel) {
            if (manageAddressListener != null) {
                if (isShareAddress) {
                    hideActionView()
                } else {
                    showIconShare()
                    setPrimaryButton(data)
                }
                binding.cbSelectAddress.gone()
            } else if (addressSharedListener != null) {
                showIconGift()
                setAddressSharedButton()
                binding.cbSelectAddress.visible()
            }
        }

        private fun showIconShare() {
            binding.iconShare.visible()
            binding.imgGift.gone()
        }

        private fun showIconGift() {
            binding.iconShare.gone()
            binding.imgGift.visible()
            binding.imgGift.setImageUrl(IMAGE_GIFT_CARD)
        }

        private fun hideActionView() {
            binding.iconShare.gone()
            binding.imgGift.gone()
            binding.btnPrimary.gone()
            binding.btnSecondary.gone()
            binding.lblMainAddress.gone()
        }

        private fun setPrimaryButton(peopleAddress: RecipientAddressModel) {
            binding.btnPrimary.visible()
            if (peopleAddress.addressStatus == 2) {
                binding.lblMainAddress.visible()
                binding.btnSecondary.gone()
            } else {
                binding.lblMainAddress.gone()
                binding.btnSecondary.visible()
            }
        }

        private fun setAddressSharedButton() {
            binding.btnPrimary.gone()
            binding.lblMainAddress.gone()
            binding.btnSecondary.gone()
        }

        private fun getShownPhoneNumber(phoneNumber: String): String {
            return if (addressSharedListener != null) {
                phoneNumber.hidePhone()
            } else {
                phoneNumber
            }
        }

        private fun getShownAddress(address: String): String {
            return if (addressSharedListener != null) {
                address.hideAddress()
            } else {
                address
            }
        }

        private fun String.hidePhone(): String {
            val chars = this.toCharArray()

            forEachIndexed { index, _ ->
                if (index.isModulusTwo()) chars[index] = '*'
            }

            return String(chars)
        }

        private fun Int.isModulusTwo(): Boolean {
            return this > 1 && this % 2 == 0
        }

        private fun String.hideAddress(): String {
            val chars = this.toCharArray()

            forEachIndexed { index, _ ->
                if (index > INDEX_SHOW_CHARACTER)
                    chars[index] = '*'
            }

            return String(chars)
        }

        private fun setVisibility(peopleAddress: RecipientAddressModel) {
            if (peopleAddress.latitude.isNullOrZero() || peopleAddress.longitude.isNullOrZero()) {
                val colorGrey = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
                binding.imgLocationState.setImage(IconUnify.LOCATION_OFF, colorGrey, colorGrey)
                binding.tvPinpointState.text = itemView.context.getString(R.string.no_pinpoint)
            } else {
                val colorGreen = ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                binding.imgLocationState.setImage(IconUnify.LOCATION, colorGreen, colorGreen)
                binding.tvPinpointState.text = itemView.context.getString(R.string.pinpoint)
            }
        }

        private fun String?.isNullOrZero(): Boolean {
            return this.isNullOrEmpty() || this == "0.0"
        }

        private fun setListener(peopleAddress: RecipientAddressModel) {
            manageAddressListener?.apply {
                binding.btnPrimary.setOnClickListener {
                    onManageAddressEditClicked(peopleAddress)
                }
                binding.btnSecondary.setOnClickListener {
                    onManageAddressLainnyaClicked(peopleAddress)
                }
                binding.cardAddress.setOnClickListener {
                    isItemClicked = true
                    notifyItemChanged(selectedPos)
                    selectedPos = layoutPosition
                    notifyItemChanged(layoutPosition)
                    manageAddressListener.onAddressItemSelected(peopleAddress)
                }
                binding.iconShare.setOnClickListener {
                    onShareAddressClicked(peopleAddress)
                }
            }

            addressSharedListener?.apply {
                binding.cbSelectAddress.setOnCheckedChangeListener { _, isChecked ->
                    onCheckedChangeListener(layoutPosition, isChecked)
                }
            }
        }
    }

    companion object {
        private const val IMAGE_GIFT_CARD = "https://images.tokopedia.net/img/android/shareaddress/gift%20card%20-%20color.png"
        private const val INDEX_SHOW_CHARACTER = 4
    }
}