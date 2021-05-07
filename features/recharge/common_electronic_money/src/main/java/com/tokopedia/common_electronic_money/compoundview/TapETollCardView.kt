package com.tokopedia.common_electronic_money.compoundview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.common_electronic_money.R
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import org.jetbrains.annotations.NotNull

/**
 * Created by Rizky on 15/05/18.
 */
class TapETollCardView @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                 defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val textTitle: Typography
    private val textLabel: Typography
    private val lottieAnimationView: LottieAnimationView
    private val buttonTryAgain: UnifyButton
    private val imageviewError: ImageView
    private val globalError: GlobalError
    private var issuerId: Int = 0

    private lateinit var listener: OnTapEtoll

    fun setListener(listener: OnTapEtoll) {
        this.listener = listener
    }

    fun setIssuerId(issuerId: Int) {
        this.issuerId = issuerId
    }

    init {
        val view = View.inflate(context, R.layout.view_tap_emoney_card, this)

        textTitle = view.findViewById(R.id.text_title)
        textLabel = view.findViewById(R.id.text_label)
        lottieAnimationView = view.findViewById(R.id.lottie_animation_view)
        buttonTryAgain = view.findViewById(R.id.button_try_again)
        imageviewError = view.findViewById(R.id.imageview_error)
        globalError = view.findViewById(R.id.emoney_global_error)
    }

    fun showLoading() {
        textTitle.text = resources.getString(R.string.emoney_reading_card_label_title)
        textTitle.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700))
        textLabel.text = resources.getString(R.string.emoney_reading_card_label_message)
        lottieAnimationView.visibility = View.VISIBLE
        lottieAnimationView.clearAnimation()
        lottieAnimationView.setAnimation("emoney_loading.json")
        lottieAnimationView.playAnimation()
        imageviewError.visibility = View.GONE
        buttonTryAgain.visibility = View.GONE
    }

    fun showInitialState() {
        globalError.hide()
        textTitle.show()
        textLabel.show()
        textTitle.text = resources.getString(R.string.emoney_tap_card_instruction_title)
        textTitle.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700))
        textLabel.text = resources.getString(R.string.emoney_tap_card_instruction_message)
        lottieAnimationView.visibility = View.VISIBLE
        lottieAnimationView.clearAnimation()
        lottieAnimationView.setAnimation("emoney_animation.json")
        lottieAnimationView.playAnimation()
        imageviewError.visibility = View.GONE
        buttonTryAgain.visibility = View.GONE
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)

        if (visibility == View.GONE) {
            showInitialState()
        }
    }

    fun showErrorState(errorMessageTitle: String, errorMessageLabel: String,
                       imageUrl:String, isButtonShow: Boolean) {
        textTitle.text = errorMessageTitle
        textLabel.text = errorMessageLabel
        lottieAnimationView.visibility = View.GONE
        imageviewError.visibility = View.VISIBLE
        buttonTryAgain.visibility = if(isButtonShow) View.VISIBLE else View.GONE

        if(!imageUrl.isNullOrEmpty()){
            imageviewError.loadImage(imageUrl){
                setPlaceHolder(R.drawable.emoney_ic_nfc_inactive_placeholder)
            }
        } else {
            imageviewError.loadImage(resources.getDrawable(R.drawable.emoney_revamp_connection_issue))
        }


        buttonTryAgain.setOnClickListener {
            showInitialState()
            listener.tryAgainTopup(issuerId)
        }
    }

    fun showErrorDeviceUnsupportedState(errorMessage: String) {
        textTitle.text = resources.getString(R.string.emoney_nfc_device_not_support)
        textLabel.text = errorMessage
        lottieAnimationView.visibility = View.GONE
        imageviewError.visibility = View.VISIBLE
        imageviewError.loadImage(resources.getString(R.string.emoney_nfc_not_found)){
            setPlaceHolder(R.drawable.emoney_ic_nfc_inactive_placeholder)
        }
        buttonTryAgain.visibility = View.GONE
    }

    fun showGlobalError(errorMessageTitle: String, errorMessageLabel: String,
                        imageUrl:String){
        textTitle.hide()
        textLabel.hide()
        lottieAnimationView.hide()
        imageviewError.hide()
        buttonTryAgain.hide()
        globalError.show()
        globalError.apply {
            errorTitle.text = errorMessageTitle
            errorDescription.text = errorMessageLabel
            errorIllustration.loadImage(resources.getDrawable(R.drawable.emoney_revamp_connection_issue))

            setActionClickListener {
                showInitialState()
                listener.tryAgainTopup(issuerId)
            }
        }
    }

    interface OnTapEtoll {
        fun tryAgainTopup(issuerId: Int)
    }
}
