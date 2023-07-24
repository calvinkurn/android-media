package com.tokopedia.home_account.view.adapter.viewholder

import android.content.Context
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.R
import com.tokopedia.home_account.Utils
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.data.model.ProfileDataView
import com.tokopedia.home_account.data.model.TierData
import com.tokopedia.home_account.databinding.HomeAccountItemProfileBinding
import com.tokopedia.home_account.view.SpanningLinearLayoutManager
import com.tokopedia.home_account.view.adapter.HomeAccountBalanceAndPointAdapter
import com.tokopedia.home_account.view.adapter.HomeAccountMemberAdapter
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusCons
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusListener
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusParam
import com.tokopedia.utils.image.ImageUtils
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ProfileViewHolder(
    itemView: View,
    val listener: HomeAccountUserListener,
    val tokopediaPlusListener: TokopediaPlusListener,
    private val balanceAndPointAdapter: HomeAccountBalanceAndPointAdapter?,
    private val memberAdapter: HomeAccountMemberAdapter?,
) : BaseViewHolder(itemView) {

    private val binding: HomeAccountItemProfileBinding? by viewBinding()

    fun getMemberTitle(): String =
        binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutTitle?.text.toString()

    fun bind(profile: ProfileDataView) {
        binding?.homeAccountProfileSection?.accountUserItemProfileName?.text = profile.name
        if (profile.phone.isNotEmpty()) {
            binding?.homeAccountProfileSection?.accountUserItemProfilePhone?.text =
                Utils.formatPhoneNumber(profile.phone)
        } else {
            binding?.homeAccountProfileSection?.accountUserItemProfilePhone?.hide()
            binding?.homeAccountProfileSection?.accountUserItemProfileName?.run {
                binding?.homeAccountProfileSection?.accountUserItemProfileName?.setPadding(
                    paddingLeft,
                    ProfileViewHolder.TOP_PAD,
                    paddingRight,
                    paddingBottom
                )
            }
        }

        if (profile.name.toLowerCase().contains(DEFAULT_NAME)) {
            binding?.homeAccountProfileSection?.accountUserItemProfileIconWarningName?.show()
            binding?.homeAccountProfileSection?.accountUserItemProfileIconWarningName?.setOnClickListener {
                listener.onIconWarningClicked(
                    profile
                )
            }
        } else binding?.homeAccountProfileSection?.accountUserItemProfileIconWarningName?.hide()

        if (profile.phone != profile.email) {
            binding?.homeAccountProfileSection?.accountUserItemProfileEmail?.text = profile.email
        }
        binding?.homeAccountProfileSection?.accountUserItemProfileEdit?.setOnClickListener { listener.onEditProfileClicked() }
        binding?.homeAccountProfileSection?.root?.setOnClickListener { listener.onProfileClicked() }

        binding?.homeAccountProfileSection?.accountUserItemProfileAvatar?.let {
            loadImage(
                it,
                profile.avatar
            )
        }

        setupMemberAdapter(itemView)
        setupBalanceAndPointAdapter(itemView)

        setBackground(itemView.context)
        listener.onItemViewBinded(adapterPosition, itemView, profile)

        setupMemberSection(profile.memberStatus)
        memberAdapter?.let { memberAdapter ->
            listener.onProfileAdapterReady(memberAdapter)
        }

        generateLinkingButton(profile)

        binding?.tokopediaPlusWidget?.apply {
            listener = tokopediaPlusListener
            if (profile.isSuccessGetTokopediaPlusData) {
                setContent(
                    TokopediaPlusParam(
                        TokopediaPlusCons.SOURCE_ACCOUNT_PAGE,
                        profile.tokopediaPlusWidget
                    )
                )
            } else {
                onError()
            }
        }
    }

    private fun generateLinkingButton(profile: ProfileDataView) {
        val isAddPhone = !profile.offerInterruptData.offers.none {
            it.name == AccountConstants.OfferInterruptionList.OFFER_PHONE
        }
        val isPhoneVerify = !profile.offerInterruptData.offers.none {
            it.name == AccountConstants.OfferInterruptionList.OFFER_VERIFY_PHONE
        }

        if (isAddPhone) {
            renderAddPhoneButton()
            return
        }

        if (isPhoneVerify && profile.phone.isNotEmpty()) {
            renderPhoneVerifyButton(profile.phone)
            return
        }

        renderAccountLinkingButton(profile)
    }

    private fun renderAddPhoneButton() {
        binding?.homeAccountProfileSection?.apply {
            accountUserItemProfileLinkStatus.hide()
            accountUserItemProfilePhone.hide()
            labelPhoneVerify.hide()
            linkAccountProfileBtn.apply {
                text = context.resources.getString(R.string.text_add_phone)
                setDrawable(getIconUnifyDrawable(
                    context,
                    IconUnify.PROTECTION,
                    ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)),
                    UnifyButton.DrawablePosition.LEFT
                )
                setOnClickListener {
                    listener.onAddPhoneClicked()
                }
            }.show()
        }
    }

    private fun renderPhoneVerifyButton(phoneNumber: String) {
        binding?.homeAccountProfileSection?.apply {
            accountUserItemProfileLinkStatus.hide()
            labelPhoneVerify.show()
            accountUserItemProfilePhone.apply {
                text = Utils.formatPhoneNumber(phoneNumber)
            }.show()

            linkAccountProfileBtn.apply {
                text = context.resources.getString(R.string.text_verify_phone)
                setDrawable(getIconUnifyDrawable(
                    context,
                    IconUnify.PROTECTION,
                    ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)),
                    UnifyButton.DrawablePosition.LEFT
                )
                setOnClickListener {
                    listener.onVerifyPhoneCLicked(phoneNumber)
                }
            }.show()
        }
    }

    private fun renderAccountLinkingButton(profile: ProfileDataView) {
        binding?.homeAccountProfileSection?.apply {
            labelPhoneVerify.hide()

            if (profile.isShowLinkStatus) {
                linkAccountProfileBtn.setOnClickListener {
                    listener.onLinkingAccountClicked(profile.isLinked)
                }
                if (profile.isLinked) {
                    linkAccountProfileBtn.hide()
                    accountUserItemProfileLinkStatus.show()
                } else {
                    accountUserItemProfileLinkStatus.hide()
                    linkAccountProfileBtn.show()
                }
            } else {
                linkAccountProfileBtn.hide()
                accountUserItemProfileLinkStatus.hide()
            }
        }
    }

    private fun setupMemberSection(tierData: TierData) {
        if (tierData.nameDesc.isNotEmpty()) {
            binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutTitle?.text =
                tierData.nameDesc
        }

        if (tierData.imageURL.isNotEmpty()) {
            binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutTitle?.setMargin(
                AccountConstants.DIMENSION.LAYOUT_TITLE_LEFT_MARGIN,
                0,
                0,
                0
            )
            binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutMemberIcon?.show()
            binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutMemberIcon?.setImageUrl(
                tierData.imageURL
            )
        } else {
            binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutMemberIcon?.hide()
        }
    }

    private fun setBackground(context: Context) {
        try {
            if (context.isDarkMode()) {
                MethodChecker.setBackground(
                    binding?.accountUserItemProfileContainer,
                    VectorDrawableCompat.create(
                        context.resources,
                        R.drawable.ic_account_backdrop_dark,
                        context.theme
                    )
                )
            } else {
                MethodChecker.setBackground(
                    binding?.accountUserItemProfileContainer,
                    VectorDrawableCompat.create(
                        context.resources,
                        R.drawable.ic_account_backdrop,
                        context.theme
                    )
                )
            }
        } catch (e: Exception) {
        }
    }

    private fun loadImage(imageView: ImageView, imageUrl: String) {
        ImageUtils.loadImageCircleWithPlaceHolder(imageView.context, imageView, imageUrl)
    }

    private fun setupBalanceAndPointAdapter(itemView: View) {
        binding?.homeAccountProfileBalanceAndPointSection?.homeAccountViewMore?.show()
        binding?.homeAccountProfileBalanceAndPointSection?.homeAccountViewMore?.setOnClickListener {
            listener.onSettingItemClicked(
                CommonDataView(
                    id = AccountConstants.SettingCode.SETTING_VIEW_ALL_BALANCE,
                    applink = ApplinkConstInternalUserPlatform.FUNDS_AND_INVESTMENT
                )
            )
        }

        binding?.homeAccountProfileBalanceAndPointSection?.homeAccountBalanceAndPointRv?.adapter =
            balanceAndPointAdapter
        binding?.homeAccountProfileBalanceAndPointSection?.homeAccountBalanceAndPointRv?.setHasFixedSize(
            true
        )
        val layoutManager = SpanningLinearLayoutManager(
            binding?.homeAccountProfileBalanceAndPointSection?.homeAccountBalanceAndPointRv?.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val verticalDivider =
            ContextCompat.getDrawable(itemView.context, R.drawable.vertical_divider)
        if (itemView.context?.isDarkMode() == true) {
            verticalDivider?.mutate()?.setColorFilter(
                itemView.resources.getColor(R.color.vertical_divider_dms_dark),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            verticalDivider?.mutate()?.setColorFilter(
                itemView.resources.getColor(R.color.vertical_divider_dms_light),
                PorterDuff.Mode.SRC_IN
            )
        }
        val dividerItemDecoration = DividerItemDecoration(
            binding?.homeAccountProfileBalanceAndPointSection?.homeAccountBalanceAndPointRv?.context,
            layoutManager.orientation
        )

        verticalDivider?.run {
            dividerItemDecoration.setDrawable(this)
        }

        binding?.homeAccountProfileBalanceAndPointSection?.homeAccountBalanceAndPointRv?.itemDecorationCount?.let {
            if (it < 1) {
                binding?.homeAccountProfileBalanceAndPointSection?.homeAccountBalanceAndPointRv?.addItemDecoration(
                    dividerItemDecoration
                )
            }
        }

        binding?.homeAccountProfileBalanceAndPointSection?.homeAccountBalanceAndPointRv?.layoutManager =
            layoutManager

        binding?.homeAccountProfileBalanceAndPointSection?.homeAccountBalanceAndPointRv?.isLayoutFrozen =
            true
    }

    private fun setupMemberAdapter(itemView: View) {
        binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutMemberForward?.setOnClickListener {
            listener.onSettingItemClicked(
                CommonDataView(
                    id = AccountConstants.SettingCode.SETTING_MORE_MEMBER,
                    applink = ApplinkConst.TOKOPOINTS
                )
            )
        }

        binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutRv?.adapter = memberAdapter
        binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutRv?.setHasFixedSize(true)
        val layoutManager = SpanningLinearLayoutManager(
            binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutRv?.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val verticalDivider =
            ContextCompat.getDrawable(itemView.context, R.drawable.vertical_divider)
        if (itemView.context?.isDarkMode() == true) {
            verticalDivider?.mutate()?.setColorFilter(
                itemView.resources.getColor(R.color.vertical_divider_dms_dark),
                PorterDuff.Mode.SRC_IN
            )
        } else {
            verticalDivider?.mutate()?.setColorFilter(
                itemView.resources.getColor(R.color.vertical_divider_dms_light),
                PorterDuff.Mode.SRC_IN
            )
        }
        val dividerItemDecoration = DividerItemDecoration(
            binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutRv?.context,
            layoutManager.orientation
        )

        verticalDivider?.run {
            dividerItemDecoration.setDrawable(this)
        }

        binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutRv?.itemDecorationCount?.let {
            if (it < 1) {
                binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutRv?.addItemDecoration(
                    dividerItemDecoration
                )
            }
        }

        binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutRv?.layoutManager =
            layoutManager

        binding?.homeAccountProfileMemberSection?.homeAccountMemberLayoutRv?.isLayoutFrozen = true
    }

    companion object {
        const val TOP_PAD = 8
        val LAYOUT = R.layout.home_account_item_profile
        private const val DEFAULT_NAME = "toppers-"
//        private const val ADD_PHONE = "Tambah Nomor HP"
//        private const val VERIFY_PHONE = "Verifikasi Nomor HP"
    }
}
