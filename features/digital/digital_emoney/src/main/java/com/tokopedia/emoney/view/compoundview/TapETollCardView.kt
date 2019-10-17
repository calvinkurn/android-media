package com.tokopedia.emoney.view.compoundview

import android.content.Context
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.emoney.R
import org.jetbrains.annotations.NotNull

/**
 * Created by Rizky on 15/05/18.
 */
class TapETollCardView @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                 defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val textTitle: TextView
    private val textLabel: TextView
    private val lottieAnimationView: LottieAnimationView
    private val buttonTryAgain: AppCompatButton
    private val imageviewError: ImageView

    init {
        val view = View.inflate(context, R.layout.view_tap_emoney_card, this)

        textTitle = view.findViewById(R.id.text_title)
        textLabel = view.findViewById(R.id.text_label)
        lottieAnimationView = view.findViewById(R.id.lottie_animation_view)
        buttonTryAgain = view.findViewById(R.id.button_try_again)
        imageviewError = view.findViewById(R.id.imageview_error)

        buttonTryAgain.setOnClickListener { showInitialState() }
    }

    fun showLoading() {
        textTitle.text = resources.getString(R.string.emoney_reading_card_label_title)
        textTitle.setTextColor(resources.getColor(R.color.black))
        textLabel.text = resources.getString(R.string.emoney_reading_card_label_message)
        lottieAnimationView.visibility = View.VISIBLE
        lottieAnimationView.clearAnimation()
        lottieAnimationView.setAnimation("emoney_loading.json")
        lottieAnimationView.playAnimation()
        imageviewError.visibility = View.GONE
        buttonTryAgain.visibility = View.GONE
    }

    fun showInitialState() {
        textTitle.text = resources.getString(R.string.emoney_tap_card_instruction_title)
        textTitle.setTextColor(resources.getColor(R.color.black))
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

    fun showErrorState(errorMessage: String) {
        textTitle.text = resources.getString(R.string.emoney_tap_card_instruction_title)
        textTitle.setTextColor(resources.getColor(R.color.red_600))
        textLabel.text = errorMessage
        lottieAnimationView.visibility = View.GONE
        imageviewError.visibility = View.VISIBLE
        buttonTryAgain.visibility = View.VISIBLE
    }
}
