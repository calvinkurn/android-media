package com.tokopedia.checkout.view.viewholder

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ViewItemShipmentRecipientAddressBinding
import com.tokopedia.checkout.domain.model.cartshipmentform.AddressesData
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.TRADE_IN_DROP_OFF_IMAGE_URL
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.purchase_platform.common.utils.Utils.getHtmlFormat
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifyprinciples.Typography.Companion.HEADING_5
import java.util.*

class ShipmentRecipientAddressViewHolder(private val binding: ViewItemShipmentRecipientAddressBinding, private val shipmentAdapterActionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(binding.root) {

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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            binding.rlShipmentRecipientAddressLayout.foreground = ContextCompat.getDrawable(binding.rlShipmentRecipientAddressLayout.context, com.tokopedia.purchase_platform.common.R.drawable.fg_enabled_item)
        }
        showOnboarding(isShowOnboarding)
    }

    private fun renderTradeInAddressWithTabs(recipientAddress: RecipientAddressModel) {
        binding.tvShipmentAddress.setText(R.string.lebal_trade_in_address_mode)
        binding.layoutTradeInOption.visibility = View.GONE
        binding.tvShipmentAddress.setType(HEADING_5)
        binding.imgButtonTradeInInfo.visibility = View.GONE
        binding.separator.visibility = View.GONE
        binding.imgButtonTradeInInfo.setOnClickListener {
            shipmentAdapterActionListener.onClickTradeInInfo()
        }
        if (binding.tabUnifyTradeInAddress.getUnifyTabLayout().tabCount == 0) {
            binding.tabUnifyTradeInAddress.addNewTab(binding.tabUnifyTradeInAddress.context.getString(R.string.label_tab_trade_in_address_deliver))
            binding.tabUnifyTradeInAddress.addNewTab(binding.tabUnifyTradeInAddress.context.getString(R.string.label_tab_trade_in_address_pickup))
        }
        renderSelectedTab(recipientAddress)
        setChipTabsClickListener(recipientAddress)
        renderDisabledTab(recipientAddress)
        binding.tabUnifyTradeInAddress.getUnifyTabLayout().addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
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
        binding.layoutAddressDropOff.tvChangeDropOff.setOnClickListener {
            shipmentAdapterActionListener.onChangeTradeInDropOffClicked(recipientAddress.latitude, recipientAddress.longitude)
        }
    }

    private fun renderDisabledTab(recipientAddress: RecipientAddressModel) {
        if (recipientAddress.disabledAddress != null && !recipientAddress.disabledAddress.isEmpty()) {
            if (recipientAddress.disabledAddress.contains(AddressesData.DEFAULT_ADDRESS)) {
                binding.chipsTradeInNormal.chipType = ChipsUnify.TYPE_DISABLE
                binding.chipsTradeInNormal.isClickable = false
                binding.chipsTradeInNormal.setOnClickListener(null)
            }
            if (recipientAddress.disabledAddress.contains(AddressesData.TRADE_IN_ADDRESS)) {
                binding.chipsTradeInDropOff.chipType = ChipsUnify.TYPE_DISABLE
                binding.chipsTradeInDropOff.isClickable = false
                binding.chipsTradeInNormal.setOnClickListener(null)
            }
        }
    }

    private fun renderSelectedTab(recipientAddress: RecipientAddressModel) {
        if (recipientAddress.selectedTabIndex == 0) {
            if (binding.tabUnifyTradeInAddress.getUnifyTabLayout().tabCount > 0) {
                recipientAddress.isIgnoreSelectionAction = true
                binding.tabUnifyTradeInAddress.getUnifyTabLayout().getTabAt(0)?.select()
                renderTradeInDeliveryTab(recipientAddress)
                setTradeInDefaultAddress()
            }
        } else {
            if (binding.tabUnifyTradeInAddress.getUnifyTabLayout().tabCount > 1) {
                recipientAddress.isIgnoreSelectionAction = true
                binding.tabUnifyTradeInAddress.getUnifyTabLayout().getTabAt(1)?.select()
                renderTradeInPickUpTab(recipientAddress)
                setTradeInDropOffAddress()
            }
        }
    }

    private fun setChipTabsClickListener(recipientAddress: RecipientAddressModel) {
        binding.chipsTradeInNormal.setOnClickListener {
            shipmentAdapterActionListener.onSwapInUserAddress()
            val tab = binding.tabUnifyTradeInAddress.tabLayout.getTabAt(0)
            if (tab != null) {
                recipientAddress.isIgnoreSelectionAction = false
                setTradeInDefaultAddress()
                tab.select()
            }
        }
        binding.chipsTradeInDropOff.setOnClickListener {
            shipmentAdapterActionListener.onClickSwapInIndomaret()
            val tab = binding.tabUnifyTradeInAddress.tabLayout.getTabAt(1)
            if (tab != null) {
                recipientAddress.isIgnoreSelectionAction = false
                setTradeInDropOffAddress()
                tab.select()
            }
        }
    }

    private fun setTradeInDefaultAddress() {
        binding.chipsTradeInNormal.chipType = ChipsUnify.TYPE_SELECTED
        if (binding.chipsTradeInDropOff.chipType != null && binding.chipsTradeInDropOff.chipType == ChipsUnify.TYPE_SELECTED) {
            binding.chipsTradeInDropOff.chipType = ChipsUnify.TYPE_NORMAL
        }
    }

    private fun setTradeInDropOffAddress() {
        binding.chipsTradeInDropOff.chipType = ChipsUnify.TYPE_SELECTED
        if (binding.chipsTradeInNormal.chipType != null && binding.chipsTradeInNormal.chipType == ChipsUnify.TYPE_SELECTED) {
            binding.chipsTradeInNormal.chipType = ChipsUnify.TYPE_NORMAL
        }
    }

    private fun renderTradeInPickUpTab(recipientAddress: RecipientAddressModel) {
        binding.layoutAddressNormal.root.visibility = View.GONE
        binding.layoutAddressDropOff.root.visibility = View.VISIBLE
        if (TextUtils.isEmpty(recipientAddress.dropOffAddressName)) {
            binding.layoutAddressDropOff.tvDropOffAddressTitle.visibility = View.GONE
            binding.layoutAddressDropOff.tvDropOffAddressDescription.visibility = View.GONE
            binding.layoutAddressDropOff.imgPinpoint.visibility = View.GONE
            binding.layoutAddressDropOff.tvChangeDropOff.visibility = View.GONE
            binding.layoutAddressDropOff.emptyStateDropOff.visibility = View.VISIBLE
            binding.layoutAddressDropOff.emptyStateDropOff.setTitle(binding.layoutAddressDropOff.emptyStateDropOff.context.getString(R.string.label_drop_off_empty_title))
            binding.layoutAddressDropOff.emptyStateDropOff.setDescription(binding.layoutAddressDropOff.emptyStateDropOff.context.getString(R.string.label_drop_off_empty_description))
            binding.layoutAddressDropOff.emptyStateDropOff.setPrimaryCTAText(binding.layoutAddressDropOff.emptyStateDropOff.context.getString(R.string.label_drop_off_cta))
            binding.layoutAddressDropOff.emptyStateDropOff.setPrimaryCTAClickListener {
                shipmentAdapterActionListener.onChangeTradeInDropOffClicked(recipientAddress.latitude, recipientAddress.longitude)
            }
            binding.layoutAddressDropOff.emptyStateDropOff.setImageUrl(TRADE_IN_DROP_OFF_IMAGE_URL)
        } else {
            binding.layoutAddressDropOff.tvDropOffAddressTitle.visibility = View.VISIBLE
            binding.layoutAddressDropOff.tvDropOffAddressDescription.visibility = View.VISIBLE
            binding.layoutAddressDropOff.tvDropOffAddressTitle.text = recipientAddress.dropOffAddressName
            binding.layoutAddressDropOff.tvDropOffAddressDescription.text = recipientAddress.dropOffAddressDetail
            binding.layoutAddressDropOff.imgPinpoint.visibility = View.VISIBLE
            binding.layoutAddressDropOff.tvChangeDropOff.visibility = View.VISIBLE
            binding.layoutAddressDropOff.emptyStateDropOff.visibility = View.GONE
        }
    }

    private fun renderTradeInDeliveryTab(recipientAddress: RecipientAddressModel) {
        binding.layoutAddressDropOff.root.visibility = View.GONE
        binding.layoutAddressNormal.root.visibility = View.VISIBLE
        renderBasicAddress(recipientAddress)
    }

    private fun renderNormalAddress(recipientAddress: RecipientAddressModel) {
        binding.tvShipmentAddress.setText(R.string.label_showcase_address_title)
        binding.layoutTradeInOption.visibility = View.GONE
        binding.imgButtonTradeInInfo.visibility = View.GONE
        binding.layoutAddressDropOff.root.visibility = View.GONE
        binding.layoutAddressNormal.root.visibility = View.VISIBLE
        renderBasicAddress(recipientAddress)
    }

    private fun renderBasicAddress(recipientAddress: RecipientAddressModel) {
        binding.layoutAddressNormal.tvAddressStatus.visibility = View.GONE
        if (recipientAddress.addressStatus == 2) {
            binding.layoutAddressNormal.tvAddressStatus.visibility = View.VISIBLE
        } else {
            binding.layoutAddressNormal.tvAddressStatus.visibility = View.GONE
        }
        binding.layoutAddressNormal.tvAddressName.text = getHtmlFormat(recipientAddress.addressName)
        binding.layoutAddressNormal.tvRecipientName.text = String.format(
            binding.layoutAddressNormal.tvRecipientName.context.getString(R.string.recipient_name_format),
            getHtmlFormat(recipientAddress.recipientName),
            recipientAddress.recipientPhoneNumber
        )
        binding.layoutAddressNormal.tvRecipientAddress.text = getHtmlFormat(getFullAddress(recipientAddress))
        binding.layoutAddressNormal.tvRecipientPhone.visibility = View.GONE
    }

    private fun getFullAddress(recipientAddress: RecipientAddressModel): String {
        return (
            recipientAddress.street + ", " +
                recipientAddress.destinationDistrictName + ", " +
                recipientAddress.cityName + ", " +
                recipientAddress.provinceName + ", " +
                recipientAddress.recipientPhoneNumber
            )
    }

    private fun renderChangeAddress(recipientAddress: RecipientAddressModel) {
        binding.separatorBottom.visibility = View.GONE
        binding.space.visibility = View.VISIBLE
        if (recipientAddress.selectedTabIndex == 0) {
            binding.tvChangeAddressTop.visibility = View.VISIBLE
            binding.tvShipmentAddress.visibility = View.VISIBLE
            binding.tvChangeAddressTop.setOnClickListener {
                shipmentAdapterActionListener.onChangeAddress()
            }
        } else {
            binding.tvChangeAddressTop.visibility = View.GONE
            binding.tvShipmentAddress.visibility = View.GONE
        }
    }

    private fun showOnboarding(isShowOnboarding: Boolean) {
        if (!isShowOnboarding) return
        val hasShownOnboarding = localCacheHandler.getBoolean(KEY_HAS_SHOWN_CHECKOUT_ADDRESS_ONBOARDING, false)
        if (hasShownOnboarding) return

        itemView.context.let {
            val onboardingItems = ArrayList<CoachMark2Item>().apply {
                add(
                    CoachMark2Item(
                        binding.rlShipmentRecipientAddressLayout,
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
