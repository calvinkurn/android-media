package com.tokopedia.activation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.activation.R
import com.tokopedia.activation.di.ActivationPageComponent
import com.tokopedia.activation.model.ActivationPageState
import com.tokopedia.activation.model.ShopFeatureModel
import com.tokopedia.activation.util.ActivationPageTouchListener
import com.tokopedia.activation.util.ICON_CHECK
import com.tokopedia.activation.util.ICON_COD
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


class ActivationPageFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModel: ActivationPageViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ActivationPageViewModel::class.java]
    }

    private val animationDuration: Long = 300
    private var codIcon: DeferredImageView? = null
    private var codTitleText: Typography? = null
    private var codDescText: Typography? = null
    private var codImageExtend: ImageView? = null

    private var codAdvantageTitle: Typography? = null
    private var codAdvantageDesc: Typography? = null

    private var codTncTitle: Typography? = null

    private var codTncImage1: DeferredImageView? = null
    private var codTncDesc1: Typography? = null

    private var codTncImage2: DeferredImageView? = null
    private var codTncDesc2: Typography? = null

    private var codTncImage3: DeferredImageView? = null
    private var codTncDesc3: Typography? = null

    private var codSeeMore: Typography? = null

    private var codCheckbox: CheckboxUnify? = null
    private var codCheckboxTnc: Typography? = null

    private var codButtonSave: UnifyButton? = null
    private var codExpandLL: LinearLayout? = null
    private var codCheckboxRL: RelativeLayout? = null
    private var codLabel: Label? = null

    private var codValue: Boolean = false
    private var expandLayout: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_activation, container, false)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ActivationPageComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
        initViewModel()
        getShopFeature()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        codIcon = view?.findViewById(R.id.cod_icon)
        codTitleText = view?.findViewById(R.id.cod_title_text)
        codDescText = view?.findViewById(R.id.cod_desc_text)
        codImageExtend = view?.findViewById(R.id.cod_layout_extend)
        codAdvantageTitle = view?.findViewById(R.id.advantage_title_cod)
        codAdvantageDesc = view?.findViewById(R.id.advantage_desc_cod)
        codTncTitle = view?.findViewById(R.id.tnc_title_cod)
        codTncImage1 = view?.findViewById(R.id.check_icon_1)
        codTncDesc1 = view?.findViewById(R.id.tnc_decs_1)
        codTncImage2 = view?.findViewById(R.id.check_icon_2)
        codTncDesc2 = view?.findViewById(R.id.tnc_decs_2)
        codTncImage3 = view?.findViewById(R.id.check_icon_3)
        codTncDesc3 = view?.findViewById(R.id.tnc_decs_3)
        codSeeMore = view?.findViewById(R.id.see_more)
        codCheckbox = view?.findViewById(R.id.cb_tnc)
        codCheckboxTnc = view?.findViewById(R.id.checkbox_tnc)
        codButtonSave = view?.findViewById(R.id.btn_save)
        codExpandLL = view?.findViewById(R.id.cod_expand_ll)
        codLabel = view?.findViewById(R.id.lbl_cod_activation)
        codCheckboxRL = view?.findViewById(R.id.checkbox_cod_rl)

        codIcon?.loadRemoteImageDrawable(ICON_COD)
        codTncImage1?.loadRemoteImageDrawable(ICON_CHECK)
        codTncImage2?.loadRemoteImageDrawable(ICON_CHECK)
        codTncImage3?.loadRemoteImageDrawable(ICON_CHECK)

        if(expandLayout) {
            codExpandLL?.show()
        } else {
            codImageExtend?.animate()?.rotation(180f)?.duration = animationDuration
            codExpandLL?.gone()
        }

        codCheckboxTnc?.text = context?.let { HtmlLinkHelper(it, getString(R.string.tnc_checkbox_text_cod)).spannedString }
        codCheckboxTnc?.setOnTouchListener(ActivationPageTouchListener{
            goToTncWebView(it)
        })
        codTncDesc1?.text = context?.let { HtmlLinkHelper(it, getString(R.string.tnc_text_1)).spannedString }
        codTncDesc1?.setOnTouchListener(ActivationPageTouchListener {
            goToTncWebView(it)
        })
        codTncDesc2?.text = context?.let { HtmlLinkHelper(it, getString(R.string.tnc_text_2)).spannedString }
        codTncDesc3?.text = context?.let { HtmlLinkHelper(it, getString(R.string.tnc_text_3)).spannedString }
        codSeeMore?.text = context?.let { HtmlLinkHelper(it, getString(R.string.see_more_cod)).spannedString }
        codSeeMore?.setOnTouchListener(ActivationPageTouchListener {
            goToTncWebView(it)
        })
    }

    private fun initViewModel() {
        viewModel.shopFeature.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ActivationPageState.Success -> {
                    setupView(it.data)
                }

                is ActivationPageState.Fail -> {

                }

                is ActivationPageState.Loading -> {

                }
            }
        })

        viewModel.updateShopFeature.observe(viewLifecycleOwner, Observer {
            when(it) {
                is ActivationPageState.Success -> {
                    getShopFeature()
                }

                is ActivationPageState.Fail -> {

                }

                is ActivationPageState.Loading -> {

                }
            }
        })
    }

    private fun initListeners() {
        codImageExtend?.setOnClickListener {
            if(expandLayout) {
                codImageExtend?.animate()?.rotation(180f)?.duration = animationDuration
                expandLayout = false
                codExpandLL?.let { layout -> collapse(layout) }
            } else {
                codImageExtend?.animate()?.rotation(0f)?.duration = animationDuration
                expandLayout = true
                codExpandLL?.let { layout -> expand(layout) }
            }
        }


    }

    private fun setupView(data: ShopFeatureModel) {
        codValue = data.value
        if (codValue) {
            codLabel?.visible()
            codCheckboxRL?.gone()
            codButtonSave?.buttonVariant = UnifyButton.Variant.GHOST
            codButtonSave?.text = getString(R.string.cod_button_inactive)
            codButtonSave?.setOnClickListener {
                context?.let { context ->
                    DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                        setTitle(getString(R.string.dialog_title_inactive))
                        setDescription(getString(R.string.dialog_desciption_inactive))
                        setPrimaryCTAText(getString(R.string.dialog_primary_button_inactive))
                        setSecondaryCTAText(getString(R.string.dialog_secondary_button_inactive))
                        setPrimaryCTAClickListener {
                            dismiss()
                            viewModel.updateShopFeature(false)
                        }
                        setSecondaryCTAClickListener {
                            dismiss()
                        }
                    }.show()
                }
            }
        } else {
            codLabel?.gone()
            codCheckboxRL?.visible()
            codButtonSave?.buttonVariant = UnifyButton.Variant.FILLED
            codButtonSave?.isEnabled = codCheckbox?.isChecked?: false
            codButtonSave?.text = getString(R.string.cod_button_active)
            codButtonSave?.setOnClickListener {
                updateShopFeature()
            }
        }

        if(codCheckbox?.isVisible == true) {
            codCheckbox?.setOnCheckedChangeListener { _, isChecked ->
                codButtonSave?.isEnabled = isChecked
            }
        }

    }

    private fun getShopFeature() {
        viewModel.getShopFeature(userSession.shopId)
    }

    private fun updateShopFeature() {
        if (!codValue) viewModel.updateShopFeature(true) else viewModel.updateShopFeature(false)
    }

    private fun collapse(view: View) {
        val initHeight = view.measuredHeight
        val anim = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                if (interpolatedTime == 1f) {
                    view.gone()
                } else {
                    view.layoutParams.height = initHeight - (initHeight * interpolatedTime).toInt()
                    view.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        anim.duration = (initHeight / view.context.resources.displayMetrics.density).toInt().toLong()
        view.startAnimation(anim)
    }

    private fun expand(view: View) {
        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec((view.parent as View).width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = view.measuredHeight
        view.layoutParams.height = 1
        view.show()
        val anim = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                view.layoutParams.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                view.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        anim.interpolator = PathInterpolatorCompat.create(0.77f, 0f, 0.175f, 1f);
        anim.duration = (targetHeight / view.context.resources.displayMetrics.density).toInt().toLong()
        view.startAnimation(anim)
    }

    private fun goToTncWebView(link: String): Boolean {
        return RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${link}")
    }

    private fun isTncCheckboxChecked(checkBox: CheckboxUnify?): Boolean {
        if (checkBox != null) {
            if (checkBox.isVisible) {
                return checkBox.isChecked
            }
            return true
        }
        return false
    }

}