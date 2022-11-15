package com.tokopedia.privacycenter.main.section.recommendation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.SectionRecomendationAndPromoBinding
import com.tokopedia.privacycenter.main.section.BasePrivacyCenterSection

class RecommendationSection (
    context: Context?,
    private val viewModel: RecommendationViewModel,
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
    override val isShowDirectionButton: Boolean = false

    override fun initObservers() {
        lifecycleOwner?.let {
            viewModel.isShakeShakeAllowed.observe(lifecycleOwner) { isAllowed ->
                sectionViewBinding.itemShakeShake.forceToggleState(isAllowed)
            }

            viewModel.isGeolocationAllowed.observe(lifecycleOwner) { isAllowed ->
                sectionViewBinding.itemGeolocation.forceToggleState(isAllowed)
            }
        }
    }

    override fun onViewRendered() {
        showShimmering(false)
        setUpView()
        initListener()
    }

    private fun setUpView() {
        sectionViewBinding.itemShakeShake.setIcon(IconUnify.SHAKE)

        sectionViewBinding.itemGeolocation.setIcon(IconUnify.LOCATION)
    }

    private fun initListener() {
        sectionViewBinding.itemShakeShake.onToggleClicked { _, isChecked ->
            viewModel.setShakeShakePermission(isChecked)
        }

        sectionViewBinding.itemGeolocation.onToggleClicked { _, isChecked ->
            if (viewModel.isGeolocationAllowed.value != isChecked) {
                sectionViewBinding.itemGeolocation.forceToggleState(false)

                if (isChecked) {
                    showVerificationPermissionGeolocation()
                } else {
                    goToApplicationDetailActivity()
                }
            }
        }
    }

    private fun showVerificationPermissionGeolocation() {
        sectionViewBinding.root.context?.apply {
            val dialog =
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
                    }
                    setSecondaryCTAText(
                        getString(R.string.privacy_center_recommendation_dialog_button_secondary_permission_geolocation)
                    )
                    setSecondaryCTAClickListener {
                        dismiss()
                    }
                }
            dialog.show()
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

    override fun onButtonDirectionClick(view: View) {
        //none
    }

    companion object {
        const val TAG = "RecommendationAndPromoSection"
        private const val PACKAGE = "package"
    }
}
