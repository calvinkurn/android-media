package com.tokopedia.logisticseller.ui.returntoshipper.dialog

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.toDp
import com.tokopedia.logisticCommon.util.LogisticImageDeliveryHelper.loadImagePod
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.data.response.GetGeneralInfoRtsResponse
import com.tokopedia.unifyprinciples.Typography

class ReturnToShipperDialog(private val context: Context) {

    fun showRtsConfirmationDialog(
        data: GetGeneralInfoRtsResponse.GeneralInfoRtsData,
        onPrimaryCTAClickListener: (() -> Unit),
        onSecondaryCTAClickListener: (() -> Unit),
        onDismissListener: (() -> Unit)
    ) {
        showDialog(
            title = data.title,
            description = data.description,
            imageData = data.image,
            primaryCtaText = context.getString(R.string.btn_rts_request),
            secondaryCtaText = context.getString(R.string.btn_rts_help),
            dialogAction = DialogUnify.HORIZONTAL_ACTION,
            imageType = if (data.image.imageId.isNullOrBlank()) {
                DialogUnify.WITH_ICON
            } else {
                DialogUnify.WITH_ILLUSTRATION
            },
            imageIcon = R.drawable.ic_logisticseller_recshedulepickup_success,
            onPrimaryCTAClickListener = {
                onPrimaryCTAClickListener.invoke()
            },
            onSecondaryCTAClickListener = {
                onSecondaryCTAClickListener.invoke()
            },
            onDismissListener = {
                onDismissListener.invoke()
            }
        )
    }

    fun showRtsSuccessDialog(
        onDismissListener: (() -> Unit)
    ) {
        showDialog(
            title = context.getString(R.string.title_rts_success_dialog),
            description = context.getString(R.string.description_rts_success_dialog),
            primaryCtaText = context.getString(R.string.btn_rts_understand),
            dialogAction = DialogUnify.SINGLE_ACTION,
            imageType = DialogUnify.WITH_ICON,
            imageIcon = R.drawable.ic_logisticseller_recshedulepickup_success,
            onPrimaryCTAClickListener = {
                onDismissListener.invoke()
            },
            onDismissListener = {
                onDismissListener.invoke()
            }
        )
    }

    fun showRtsFailedDialog(
        onDismissListener: (() -> Unit)
    ) {
        showDialog(
            title = context.getString(R.string.title_rts_failed_dialog),
            description = context.getString(R.string.description_rts_failed_dialog),
            primaryCtaText = context.getString(R.string.btn_rts_understand),
            dialogAction = DialogUnify.SINGLE_ACTION,
            imageType = DialogUnify.WITH_ICON,
            imageIcon = R.drawable.ic_logisticseller_reschedulepickup_fail,
            onPrimaryCTAClickListener = {
                onDismissListener.invoke()
            },
            onDismissListener = {
                onDismissListener.invoke()
            }
        )
    }

    private fun showDialog(
        title: String,
        description: String,
        primaryCtaText: String,
        secondaryCtaText: String? = null,
        dialogAction: Int = DialogUnify.HORIZONTAL_ACTION,
        imageType: Int = DialogUnify.NO_IMAGE,
        onPrimaryCTAClickListener: (() -> Unit)? = null,
        onSecondaryCTAClickListener: (() -> Unit)? = null,
        onDismissListener: (() -> Unit)? = null,
        imageData: GetGeneralInfoRtsResponse.Image? = null,
        imageIcon: Int = 0
    ) {
        var isFromCTAClickListener = false

        DialogUnify(context, dialogAction, imageType).apply {
            if (DeviceScreenInfo.isTablet(context)) {
                dialogMaxWidth = getScreenWidth() / 2
            }
            setTitle(title)
            setDescription(description)
            setPrimaryCTAText(primaryCtaText)
            setSecondaryCTAText(secondaryCtaText.orEmpty())
            if (imageType == DialogUnify.WITH_ILLUSTRATION) {
                imageData?.apply {
                    dialogImage.loadImagePod(
                        context,
                        accessToken = accessToken,
                        url = urlImage,
                        drawableImageError = MethodChecker.getDrawable(
                            context,
                            com.tokopedia.unifycomponents.R.drawable.imagestate_error
                        ),
                        onReadyListener = {
                            imageDisclaimer?.takeIf { it.isNotBlank() }?.apply {
                                setImageDescription(
                                    description = imageDisclaimer
                                )
                            }
                        },
                        onFailedListener = {
                            setImageDescription(
                                description = context.getString(
                                    R.string.description_failed_load_image_rts,
                                    imageDisclaimer.orEmpty()
                                )
                            )
                        }

                    )
                }
            } else {
                setImageDrawable(imageIcon)
            }
            setPrimaryCTAClickListener {
                isFromCTAClickListener = true
                onPrimaryCTAClickListener?.invoke()
                this.dismiss()
            }
            setSecondaryCTAClickListener {
                onSecondaryCTAClickListener?.invoke()
            }
            setOnDismissListener {
                if (isFromCTAClickListener.not()) {
                    onDismissListener?.invoke()
                }
            }
            show()
        }
    }

    private fun DialogUnify.setImageDescription(description: String) {
        try {
            dialogContainer.applyConstraintSet {
                it.clear(dialogContent.id, ConstraintSet.TOP)
                it.connect(
                    dialogContent.id,
                    ConstraintSet.TOP,
                    dialogImageContainer.id,
                    ConstraintSet.BOTTOM,
                    context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
                        .toDp().toInt(),
                )
            }

            dialogContent.applyConstraintSet {
                it.clear(dialogTitle.id, ConstraintSet.TOP)
                it.connect(
                    dialogTitle.id,
                    ConstraintSet.TOP,
                    dialogChild.id,
                    ConstraintSet.BOTTOM,
                    context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_24)
                        .toDp().toInt(),
                )
            }

            val tvImageDescription = Typography(context).apply {
                text = description
                setType(Typography.SMALL)
                gravity = Gravity.CENTER
                setTextColor(
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN600
                    )
                )
            }

            (dialogChild as LinearLayout).apply {
                removeAllViews()
                addView(tvImageDescription)
            }
        } catch (e: Exception) {
            // no op
        }
    }

    private fun ConstraintLayout?.applyConstraintSet(configureConstraintSet: (ConstraintSet) -> Unit) {
        this?.let {
            val constraintSet = ConstraintSet()
            constraintSet.clone(it)
            configureConstraintSet(constraintSet)
            constraintSet.applyTo(it)
        }
    }
}
