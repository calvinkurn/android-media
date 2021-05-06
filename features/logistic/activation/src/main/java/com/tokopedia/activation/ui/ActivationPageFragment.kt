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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.activation.R
import com.tokopedia.activation.di.ActivationPageComponent
import com.tokopedia.activation.model.ActivationPageState
import com.tokopedia.activation.model.ShippingEditorModel
import com.tokopedia.activation.model.ShopFeatureModel
import com.tokopedia.activation.model.UpdateFeatureModel
import com.tokopedia.activation.util.ActivationPageTouchListener
import com.tokopedia.activation.util.COD_ACTIVE_MESSAGE
import com.tokopedia.activation.util.COD_INACTIVE_MESSAGE
import com.tokopedia.activation.util.DEFAULT_ERROR_MESSAGE
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
    private var codIcon: ImageView? = null
    private var codTitleText: Typography? = null
    private var codDescText: Typography? = null
    private var codImageExtend: ImageView? = null

    private var codAdvantageTitle: Typography? = null
    private var codAdvantageDesc: Typography? = null

    private var codTncTitle: Typography? = null

    private var codTncImage1: ImageView? = null
    private var codTncDesc1: Typography? = null

    private var codTncImage2: ImageView? = null
    private var codTncDesc2: Typography? = null

    private var codTncImage3: ImageView? = null
    private var codTncDesc3: Typography? = null

    private var codSeeMore: Typography? = null

    private var codCheckbox: CheckboxUnify? = null
    private var codCheckboxTnc: Typography? = null

    private var codButtonSave: UnifyButton? = null
    private var codExpandLL: LinearLayout? = null
    private var codCheckboxRL: RelativeLayout? = null
    private var codLabel: Label? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var itemLayout: CardUnify? = null
    private var globalError: GlobalError? = null

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
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh)
        itemLayout = view?.findViewById(R.id.item_activation_layout)
        globalError = view?.findViewById(R.id.global_error)

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
                    swipeRefreshLayout?.isRefreshing = false
                    globalError?.gone()
                    itemLayout?.visible()
                    setupView(it.data)
                }

                is ActivationPageState.Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }

                is ActivationPageState.Loading -> {
                    swipeRefreshLayout?.isRefreshing = true
                }
            }
        })

        viewModel.updateShopFeature.observe(viewLifecycleOwner, Observer {
            when(it) {
                is ActivationPageState.Success -> {
                    openDialogCoverageValidation(it.data)
                }

                is ActivationPageState.Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }

                is ActivationPageState.Loading -> {
                    swipeRefreshLayout?.isRefreshing = true
                }
            }
        })

        viewModel.activatedShipping.observe(viewLifecycleOwner, Observer {
            when(it) {
                is ActivationPageState.Success -> {
                    openDialogActivationPage(it.data)
                }

                is ActivationPageState.Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }

                is ActivationPageState.Loading -> {
                    swipeRefreshLayout?.isRefreshing = true
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
            codButtonSave?.isEnabled = true
            codButtonSave?.setOnClickListener {
                context?.let { context ->
                    DialogUnify(context, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                        setTitle(getString(R.string.dialog_title_inactive))
                        setDescription(getString(R.string.dialog_description_inactive))
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
                validateActivatedShipping()
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

    private fun validateActivatedShipping(){
        val userId = userSession.userId
        viewModel.validateActivatedShipping(userId.toInt())
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
        anim.interpolator = PathInterpolatorCompat.create(0.77f, 0f, 0.175f, 1f)
        anim.duration = (targetHeight / view.context.resources.displayMetrics.density).toInt().toLong()
        view.startAnimation(anim)
    }

    private fun openDialogActivationPage(data: ShippingEditorModel) {
        if (data.x11 == 0) {
            swipeRefreshLayout?.isRefreshing = false
            context?.let { dialog ->
                DialogUnify(dialog, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                    val primaryText = HtmlLinkHelper(context, getString(R.string.dialog_description_active)).spannedString
                    setTitle(getString(R.string.dialog_title_active))
                    if (primaryText != null) {
                        setDescription(primaryText)
                    }
                    setPrimaryCTAText(getString(R.string.dialog_primary_button_active))
                    setSecondaryCTAText(getString(R.string.dialog_secondary_button_active))
                    setPrimaryCTAClickListener {
                        viewModel.updateShopFeature(true)
                        dismiss()
                    }
                    setSecondaryCTAClickListener {
                        dismiss()
                    }
                }.show()
            }
        } else {
            viewModel.updateShopFeature(true)
        }
    }

    private fun openDialogCoverageValidation(data: UpdateFeatureModel) {
        if(!data.success) {
            swipeRefreshLayout?.isRefreshing = false
            context?.let {
                DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE).apply {
                    setTitle(getString(R.string.dialog_title_coverage))
                    setDescription(getString(R.string.dialog_description_coverage))
                    setPrimaryCTAText(getString(R.string.dialog_button_coverage))
                    setPrimaryCTAClickListener {
                        dismiss()
                    }
                }.show()
            }
        } else {
            view?.let { view ->
                if (codValue) Toaster.build(view, COD_INACTIVE_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_NORMAL).show()
                else Toaster.build(view, COD_ACTIVE_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_NORMAL).show()
            }
            getShopFeature()
        }
    }

    private fun goToTncWebView(link: String): Boolean {
        return RouteManager.route(activity, "${ApplinkConst.WEBVIEW}?url=${link}")
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)

                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.build(it, DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.build(it, throwable.message
                            ?: DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalError?.setType(type)
        globalError?.setActionClickListener {
            viewModel.getShopFeature(userSession.shopId)
        }
        itemLayout?.gone()
        globalError?.visible()
    }

}