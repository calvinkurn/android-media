package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Space
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.checkout.R
import com.tokopedia.checkout.domain.model.cartshipmentform.AddressesData
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.TRADE_IN_DROP_OFF_IMAGE_URL
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.purchase_platform.common.utils.Utils.getHtmlFormat
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.TabsUnify
import com.tokopedia.unifycomponents.UnifyImageButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.HEADING_5
import java.util.*

class ShipmentRecipientAddressViewHolder(itemView: View, private val shipmentAdapterActionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(itemView) {

    private val rlRecipientAddressLayout: RelativeLayout = itemView.findViewById(R.id.rl_shipment_recipient_address_layout)
    private val tvAddressStatus: Label = itemView.findViewById(com.tokopedia.logisticCommon.R.id.tv_address_status)
    private val tvAddressName: Typography = itemView.findViewById(com.tokopedia.logisticCommon.R.id.tv_address_name)
    private val tvRecipientName: Typography = itemView.findViewById(com.tokopedia.logisticCommon.R.id.tv_recipient_name)
    private val tvRecipientAddress: Typography = itemView.findViewById(com.tokopedia.logisticCommon.R.id.tv_recipient_address)
    private val tvRecipientPhone: Typography = itemView.findViewById(com.tokopedia.logisticCommon.R.id.tv_recipient_phone)
    private val tabUnifyTradeInAddress: TabsUnify = itemView.findViewById(R.id.tab_unify_trade_in_address)
    private val layoutAddressNormal: RelativeLayout = itemView.findViewById(R.id.layout_address_normal)
    private val layoutAddressDropOff: ConstraintLayout = itemView.findViewById(R.id.layout_address_drop_off)
    private val tvShipmentAddress: Typography = itemView.findViewById(R.id.tv_shipment_address)
    private val imgButtonTradeInInfo: UnifyImageButton = itemView.findViewById(R.id.img_button_trade_in_info)
    private val separator: View = itemView.findViewById(R.id.separator)
    private val tvDropOffAddressTitle: Typography = itemView.findViewById(R.id.tv_drop_off_address_title)
    private val tvDropOffAddressDescription: Typography = itemView.findViewById(R.id.tv_drop_off_address_description)
    private val tvChangeDropOff: Typography = itemView.findViewById(R.id.tv_change_drop_off)
    private val imgPinpoint: ImageView = itemView.findViewById(R.id.img_pinpoint)
    private val tvChangeAddressTop: Typography = itemView.findViewById(R.id.tv_change_address_top)
    private val separatorBottom: View = itemView.findViewById(R.id.separator_bottom)
    private val space: Space = itemView.findViewById(R.id.space)
    private val layoutTradeInOption: ConstraintLayout = itemView.findViewById(R.id.layout_trade_in_option)
    private val chipsTradeInNormal: ChipsUnify = itemView.findViewById(R.id.chips_trade_in_normal)
    private val chipsTradeInDropOff: ChipsUnify = itemView.findViewById(R.id.chips_trade_in_drop_off)
    private val emptyStateDropOff: EmptyStateUnify = itemView.findViewById(R.id.empty_state_drop_off)

    private val localCacheHandler: LocalCacheHandler by lazy {
        LocalCacheHandler(itemView.context, KEY_ONBOARDING_CHECKOUT_ADDRESS)
    }

    @SuppressLint("NewApi")
    fun bindViewHolder(recipientAddress: RecipientAddressModel, isShowOnboarding: Boolean) {
        renderChangeAddress(recipientAddress)
        if (recipientAddress.isTradeIn) {
            renderTradeInAddressWithTabs(recipientAddress)
        } else {
            renderNormalAddress(recipientAddress)
        }
        if (recipientAddress.isEnabled) {
            rlRecipientAddressLayout.foreground = ContextCompat.getDrawable(rlRecipientAddressLayout.context, com.tokopedia.purchase_platform.common.R.drawable.fg_enabled_item)
            showOnboarding(isShowOnboarding)
        } else {
            rlRecipientAddressLayout.foreground = ContextCompat.getDrawable(rlRecipientAddressLayout.context, com.tokopedia.purchase_platform.common.R.drawable.fg_disabled_item)
        }
    }

    private fun renderTradeInAddressWithTabs(recipientAddress: RecipientAddressModel) {
        tvShipmentAddress.setText(R.string.lebal_trade_in_address_mode)
        layoutTradeInOption.visibility = View.VISIBLE
        tvShipmentAddress.setType(HEADING_5)
        imgButtonTradeInInfo.visibility = View.VISIBLE
        separator.visibility = View.GONE
        imgButtonTradeInInfo.setOnClickListener {
            if (recipientAddress.isEnabled) {
                shipmentAdapterActionListener.onClickTradeInInfo()
            }
        }
        if (tabUnifyTradeInAddress.getUnifyTabLayout().tabCount == 0) {
            tabUnifyTradeInAddress.addNewTab(tabUnifyTradeInAddress.context.getString(R.string.label_tab_trade_in_address_deliver))
            tabUnifyTradeInAddress.addNewTab(tabUnifyTradeInAddress.context.getString(R.string.label_tab_trade_in_address_pickup))
        }
        renderSelectedTab(recipientAddress)
        setChipTabsClickListener(recipientAddress)
        renderDisabledTab(recipientAddress)
        tabUnifyTradeInAddress.getUnifyTabLayout().addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && recipientAddress.isEnabled) {
                    if (!recipientAddress.isIgnoreSelectionAction) {
                        recipientAddress.isIgnoreSelectionAction = true
                        recipientAddress.selectedTabIndex = tab.position
                        shipmentAdapterActionListener.onTradeInAddressTabChanged(position)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        tvChangeDropOff.setOnClickListener {
            if (recipientAddress.isEnabled) {
                shipmentAdapterActionListener.onChangeTradeInDropOffClicked()
            }
        }
    }

    private fun renderDisabledTab(recipientAddress: RecipientAddressModel) {
        if (recipientAddress.disabledAddress != null && !recipientAddress.disabledAddress.isEmpty()) {
            if (recipientAddress.disabledAddress.contains(AddressesData.DEFAULT_ADDRESS)) {
                chipsTradeInNormal.chipType = ChipsUnify.TYPE_DISABLE
                chipsTradeInNormal.isClickable = false
                chipsTradeInNormal.setOnClickListener(null)
            }
            if (recipientAddress.disabledAddress.contains(AddressesData.TRADE_IN_ADDRESS)) {
                chipsTradeInDropOff.chipType = ChipsUnify.TYPE_DISABLE
                chipsTradeInDropOff.isClickable = false
                chipsTradeInNormal.setOnClickListener(null)
            }
        }
    }

    private fun renderSelectedTab(recipientAddress: RecipientAddressModel) {
        if (recipientAddress.selectedTabIndex == 0) {
            if (tabUnifyTradeInAddress.getUnifyTabLayout().tabCount > 0) {
                recipientAddress.isIgnoreSelectionAction = true
                tabUnifyTradeInAddress.getUnifyTabLayout().getTabAt(0)?.select()
                renderTradeInDeliveryTab(recipientAddress)
                setTradeInDefaultAddress()
            }
        } else {
            if (tabUnifyTradeInAddress.getUnifyTabLayout().tabCount > 1) {
                recipientAddress.isIgnoreSelectionAction = true
                tabUnifyTradeInAddress.getUnifyTabLayout().getTabAt(1)?.select()
                renderTradeInPickUpTab(recipientAddress)
                setTradeInDropOffAddress()
            }
        }
    }

    private fun setChipTabsClickListener(recipientAddress: RecipientAddressModel) {
        chipsTradeInNormal.setOnClickListener { v: View? ->
            shipmentAdapterActionListener.onSwapInUserAddress()
            val tab = tabUnifyTradeInAddress.tabLayout.getTabAt(0)
            if (tab != null) {
                recipientAddress.isIgnoreSelectionAction = false
                setTradeInDefaultAddress()
                tab.select()
            }
        }
        chipsTradeInDropOff.setOnClickListener { v: View? ->
            shipmentAdapterActionListener.onClickSwapInIndomaret()
            val tab = tabUnifyTradeInAddress.tabLayout.getTabAt(1)
            if (tab != null) {
                recipientAddress.isIgnoreSelectionAction = false
                setTradeInDropOffAddress()
                tab.select()
            }
        }
    }

    private fun setTradeInDefaultAddress() {
        chipsTradeInNormal.chipType = ChipsUnify.TYPE_SELECTED
        if (chipsTradeInDropOff.chipType != null && chipsTradeInDropOff.chipType == ChipsUnify.TYPE_SELECTED) {
            chipsTradeInDropOff.chipType = ChipsUnify.TYPE_NORMAL
        }
    }

    private fun setTradeInDropOffAddress() {
        chipsTradeInDropOff.chipType = ChipsUnify.TYPE_SELECTED
        if (chipsTradeInNormal.chipType != null && chipsTradeInNormal.chipType == ChipsUnify.TYPE_SELECTED) {
            chipsTradeInNormal.chipType = ChipsUnify.TYPE_NORMAL
        }
    }

    private fun renderTradeInPickUpTab(recipientAddress: RecipientAddressModel) {
        layoutAddressNormal.visibility = View.GONE
        layoutAddressDropOff.visibility = View.VISIBLE
        if (TextUtils.isEmpty(recipientAddress.dropOffAddressName)) {
            tvDropOffAddressTitle.visibility = View.GONE
            tvDropOffAddressDescription.visibility = View.GONE
            imgPinpoint.visibility = View.GONE
            tvChangeDropOff.visibility = View.GONE
            emptyStateDropOff.visibility = View.VISIBLE
            emptyStateDropOff.setTitle(emptyStateDropOff.context.getString(R.string.label_drop_off_empty_title))
            emptyStateDropOff.setDescription(emptyStateDropOff.context.getString(R.string.label_drop_off_empty_description))
            emptyStateDropOff.setPrimaryCTAText(emptyStateDropOff.context.getString(R.string.label_drop_off_cta))
            emptyStateDropOff.setPrimaryCTAClickListener {
                shipmentAdapterActionListener.onChangeTradeInDropOffClicked()
                Unit
            }
            emptyStateDropOff.setImageUrl(TRADE_IN_DROP_OFF_IMAGE_URL)
        } else {
            tvDropOffAddressTitle.visibility = View.VISIBLE
            tvDropOffAddressDescription.visibility = View.VISIBLE
            tvDropOffAddressTitle.text = recipientAddress.dropOffAddressName
            tvDropOffAddressDescription.text = recipientAddress.dropOffAddressDetail
            imgPinpoint.visibility = View.VISIBLE
            tvChangeDropOff.visibility = View.VISIBLE
            emptyStateDropOff.visibility = View.GONE
        }
    }

    private fun renderTradeInDeliveryTab(recipientAddress: RecipientAddressModel) {
        layoutAddressDropOff.visibility = View.GONE
        layoutAddressNormal.visibility = View.VISIBLE
        renderBasicAddress(recipientAddress)
    }

    private fun renderNormalAddress(recipientAddress: RecipientAddressModel) {
        tvShipmentAddress.setText(R.string.label_showcase_address_title)
        layoutTradeInOption.visibility = View.GONE
        imgButtonTradeInInfo.visibility = View.GONE
        layoutAddressDropOff.visibility = View.GONE
        layoutAddressNormal.visibility = View.VISIBLE
        renderBasicAddress(recipientAddress)
    }

    private fun renderBasicAddress(recipientAddress: RecipientAddressModel) {
        tvAddressStatus.visibility = View.GONE
        if (recipientAddress.addressStatus == 2) {
            tvAddressStatus.visibility = View.VISIBLE
        } else {
            tvAddressStatus.visibility = View.GONE
        }
        tvAddressName.text = getHtmlFormat(recipientAddress.addressName)
        tvRecipientName.text = String.format(tvRecipientName.context.getString(R.string.recipient_name_format),
                getHtmlFormat(recipientAddress.recipientName), recipientAddress.recipientPhoneNumber)
        tvRecipientAddress.text = getHtmlFormat(getFullAddress(recipientAddress))
        tvRecipientPhone.visibility = View.GONE
    }

    private fun getFullAddress(recipientAddress: RecipientAddressModel): String {
        return (recipientAddress.street + ", "
                + recipientAddress.destinationDistrictName + ", "
                + recipientAddress.cityName + ", "
                + recipientAddress.provinceName + ", "
                + recipientAddress.recipientPhoneNumber)
    }

    private fun renderChangeAddress(recipientAddress: RecipientAddressModel) {
        separatorBottom.visibility = View.GONE
        space.visibility = View.VISIBLE
        if (recipientAddress.selectedTabIndex == 0) {
            tvChangeAddressTop.visibility = View.VISIBLE
            tvChangeAddressTop.setOnClickListener {
                if (recipientAddress.isEnabled) {
                    shipmentAdapterActionListener.onChangeAddress()
                }
            }
        } else {
            tvChangeAddressTop.visibility = View.GONE
        }
    }

    private fun showOnboarding(isShowOnboarding: Boolean) {
        if (!isShowOnboarding) return
        val hasShownOnboarding = localCacheHandler.getBoolean(KEY_HAS_SHOWN_CHECKOUT_ADDRESS_ONBOARDING, false)
        if (hasShownOnboarding) return

        itemView.context.let {
            val onboardingItems = ArrayList<CoachMark2Item>().apply {
                add(
                        CoachMark2Item(rlRecipientAddressLayout,
                                it.getString(R.string.label_showcase_address_title),
                                it.getString(R.string.label_showcase_address_message)
                        )
                )
            }

            CoachMark2(it).apply {
                showCoachMark(onboardingItems)
            }

            localCacheHandler.apply {
                putBoolean(KEY_HAS_SHOWN_CHECKOUT_ADDRESS_ONBOARDING, true)
                applyEditor()
            }
        }
    }

    companion object {
        @JvmField
        val ITEM_VIEW_RECIPIENT_ADDRESS = R.layout.view_item_shipment_recipient_address

        const val KEY_ONBOARDING_CHECKOUT_ADDRESS = "KEY_ONBOARDING_CHECKOUT_ADDRESS"
        const val KEY_HAS_SHOWN_CHECKOUT_ADDRESS_ONBOARDING = "KEY_HAS_SHOWN_CHECKOUT_ADDRESS_ONBOARDING"
    }

}