package com.tokopedia.privacycenter.ui.main.section.recommendation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.databinding.SectionRecomendationAndPromoBinding
import com.tokopedia.privacycenter.domain.GetRecommendationFriendState
import com.tokopedia.privacycenter.ui.main.analytics.MainPrivacyCenterAnalytics
import com.tokopedia.privacycenter.ui.main.section.BasePrivacyCenterSection
import com.tokopedia.privacycenter.utils.getMessage
import com.tokopedia.unifycomponents.Toaster

class RecommendationSection(
    context: Context?,
    private val viewModel: RecommendationViewModel,
    private val fragmentManager: FragmentManager,
    private val listener: Listener
) : BasePrivacyCenterSection(context) {

    interface Listener {
        fun onRequestLocationPermission()
    }

    override val sectionViewBinding: SectionRecomendationAndPromoBinding =
        SectionRecomendationAndPromoBinding.inflate(
            LayoutInflater.from(context)
        )
    override val sectionTextTitle: String =
        context?.getString(R.string.privacy_center_recommendation_title).orEmpty()
    override val sectionTextDescription: String =
        context?.getString(R.string.privacy_center_recommendation_subtitle).orEmpty()

    private var verificationEnabledDataUsageBottomSheet: RecommendationFriendBottomSheet? = null
    private var verificationDisabledDataUsageDialog: DialogUnify? = null
    private var loaderDialog: LoaderDialog? = null
    private var enableGeolocationDialog: DialogUnify? = null

    override fun initObservers() {
        lifecycleOwner?.let { lifecycle ->
            viewModel.isShakeShakeAllowed.observe(lifecycle) { isAllowed ->
                sectionViewBinding.itemShakeShake.forceToggleState(isAllowed)
            }

            viewModel.isGeolocationAllowed.observe(lifecycle) { isAllowed ->
                sectionViewBinding.itemGeolocation.forceToggleState(isAllowed)
            }

            viewModel.isRecommendationFriendAllowed.observe(lifecycle) {
                sectionViewBinding.itemRecommendationFriend.forceToggleState(it)
            }

            viewModel.getConsentSocialNetwork.observe(lifecycle) {
                when (it) {
                    is GetRecommendationFriendState.Loading -> {
                        showShimmering(true)
                    }
                    is GetRecommendationFriendState.Success -> {
                        showShimmering(false)
                    }
                    is GetRecommendationFriendState.Failed -> {
                        showLocalLoad {
                            viewModel.getConsentSocialNetwork()
                        }
                    }
                }
            }

            viewModel.setConsentSocialNetwork.observe(lifecycle) {
                when (it) {
                    is PrivacyCenterStateResult.Loading -> {
                        showLoaderDialog(true)
                    }
                    is PrivacyCenterStateResult.Success -> {
                        showLoaderDialog(false)
                    }
                    is PrivacyCenterStateResult.Fail -> {
                        showLoaderDialog(false)
                        val message = it.error.getMessage(sectionViewBinding.root.context)
                        showToasterError(message)
                    }
                }
            }
        }
    }

    override fun onViewRendered() {
        showShimmering(true)
        setUpView()
        initListener()
    }

    private fun setUpView() {
        sectionViewBinding.itemShakeShake.setIcon(IconUnify.SHAKE)

        sectionViewBinding.itemGeolocation.setIcon(IconUnify.LOCATION)

        sectionViewBinding.itemRecommendationFriend.setIcon(IconUnify.USER_ADD)
    }

    private fun initListener() {
        sectionViewBinding.itemShakeShake.onToggleClicked { _, isChecked ->
            viewModel.setShakeShakePermission(isChecked)

            MainPrivacyCenterAnalytics.sendClickOnTrackingSectionEvent(
                MainPrivacyCenterAnalytics.LABEL_TOGGLE_SHAKE_SHAKE,
                isChecked
            )
        }

        sectionViewBinding.itemGeolocation.onToggleClicked { _, isChecked ->
            if (viewModel.isGeolocationAllowed.value != isChecked) {
                MainPrivacyCenterAnalytics.sendClickOnTrackingSectionEvent(
                    MainPrivacyCenterAnalytics.LABEL_TOGGLE_GEOLOCATION,
                    isChecked
                )

                sectionViewBinding.itemGeolocation.forceToggleState(false)

                if (isChecked) {
                    showVerificationPermissionGeolocation()
                } else {
                    goToApplicationDetailActivity()
                }
            }
        }

        sectionViewBinding.itemRecommendationFriend.onToggleClicked { buttonView, isChecked ->
            if (viewModel.isRecommendationFriendAllowed.value != isChecked) {
                MainPrivacyCenterAnalytics.sendClickOnTrackingSectionEvent(
                    MainPrivacyCenterAnalytics.LABEL_TOGGLE_RECOMMENDATION_FRIEND,
                    isChecked
                )

                sectionViewBinding.itemRecommendationFriend.forceToggleState(!isChecked)

                if (!isChecked) {
                    showVerificationDisabledDataUsage()
                } else {
                    showVerificationEnabledDataUsage()
                }
            }
        }
    }

    private fun showVerificationEnabledDataUsage() {
        verificationEnabledDataUsageBottomSheet = RecommendationFriendBottomSheet()

        verificationEnabledDataUsageBottomSheet?.setOnVerificationClickedListener {
            verificationEnabledDataUsageBottomSheet?.dismiss()
            verificationEnabledDataUsageBottomSheet = null
            viewModel.setConsentSocialNetwork(true)
        }

        verificationEnabledDataUsageBottomSheet?.show(
            fragmentManager,
            TAG_BOTTOM_SHEET_VERIFICATION
        )
    }

    private fun showLoaderDialog(isShow: Boolean) {
        if (isShow) {
            loaderDialog = LoaderDialog(sectionViewBinding.root.context)
            loaderDialog?.setLoadingText("")
            loaderDialog?.show()
        } else {
            loaderDialog?.dismiss()
            loaderDialog = null
        }
    }

    private fun showVerificationDisabledDataUsage() {
        sectionViewBinding.root.context?.apply {
            verificationDisabledDataUsageDialog =
                DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)

            verificationDisabledDataUsageDialog?.apply {
                setTitle(getString(R.string.privacy_center_recommendation_friend_dialog_disabled_title))
                setDescription(getString(R.string.privacy_center_recommendation_friend_dialog_disabled_subtitle))
                setPrimaryCTAText(getString(R.string.privacy_center_recommendation_friend_dialog_primary))
                setSecondaryCTAText(getString(R.string.privacy_center_recommendation_friend_dialog_secondary))
            }

            verificationDisabledDataUsageDialog?.setPrimaryCTAClickListener {
                verificationDisabledDataUsageDialog?.dismiss()
                verificationDisabledDataUsageDialog = null
                viewModel.setConsentSocialNetwork(false)
            }

            verificationDisabledDataUsageDialog?.setSecondaryCTAClickListener {
                verificationDisabledDataUsageDialog?.dismiss()
                verificationDisabledDataUsageDialog = null
            }

            verificationDisabledDataUsageDialog?.show()
        }
    }

    private fun showVerificationPermissionGeolocation() {
        sectionViewBinding.root.context?.apply {
            enableGeolocationDialog =
                DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                    setTitle(
                        getString(R.string.privacy_center_recommendation_dialog_title_permission_geolocation)
                    )
                    setDescription(
                        getString(R.string.privacy_center_recommendation_dialog_description_permission_geolocation)
                    )
                    setPrimaryCTAText(
                        getString(R.string.privacy_center_recommendation_dialog_button_primary_permission_geolocation)
                    )
                    setPrimaryCTAClickListener {
                        listener.onRequestLocationPermission()
                        dismiss()
                        enableGeolocationDialog = null
                    }
                    setSecondaryCTAText(
                        getString(R.string.privacy_center_recommendation_dialog_button_secondary_permission_geolocation)
                    )
                    setSecondaryCTAClickListener {
                        dismiss()
                        enableGeolocationDialog = null
                    }
                }
            enableGeolocationDialog?.show()
        }
    }

    private fun goToApplicationDetailActivity() {
        sectionViewBinding.root.context.let {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts(PACKAGE, it.packageName, null)
            intent.data = uri
            it.startActivity(intent)
        }
    }

    private fun showToasterError(message: String) {
        Toaster.build(sectionViewBinding.root, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    companion object {
        const val TAG = "RecommendationAndPromoSection"
        private const val TAG_BOTTOM_SHEET_VERIFICATION = "BottomSheetRecommendationFriend"
        private const val PACKAGE = "package"
    }
}
