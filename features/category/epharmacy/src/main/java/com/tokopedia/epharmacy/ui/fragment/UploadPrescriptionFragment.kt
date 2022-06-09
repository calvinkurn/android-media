package com.tokopedia.epharmacy.ui.fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.di.EPharmacyComponent
import com.tokopedia.epharmacy.network.response.EPharmacyDataResponse
import com.tokopedia.epharmacy.viewmodel.UploadPrescriptionViewModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class UploadPrescriptionFragment : BaseDaggerFragment() {

    private var ePharmacyToolTipText : Typography? = null
    private var ePharmacyRecyclerView : RecyclerView? = null
    private var fotoResepButton : UnifyButton? = null
    private var doneButton : UnifyButton? = null

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var userSession: UserSessionInterface

    private val uploadPrescriptionViewModel: UploadPrescriptionViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(UploadPrescriptionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.epharmacy_upload_prescription_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        setUpObservers()
        initViews(view)
        initData()
        uploadPrescriptionViewModel.getEPharmacyDetail("0")
    }

    private fun initArguments() {

    }

    private fun setUpObservers() {
        observerEPharmacyDetail()
    }

    private fun observerEPharmacyDetail(){
        uploadPrescriptionViewModel.productDetailLiveDataResponse.observe(viewLifecycleOwner){
            when (it) {
                is Success -> {
                    onSuccessEPharmacyData(it)
                }
                is Fail -> {
                    onFailEPharmacyData(it)
                }
            }
        }
    }

    private fun onFailEPharmacyData(it: Fail) {
        when (it.throwable) {

        }
    }

    private fun onSuccessEPharmacyData(it: Success<EPharmacyDataResponse>) {

    }

    private fun initViews(view: View) {
        view.apply {
            ePharmacyToolTipText = findViewById(R.id.tooltip)
            ePharmacyRecyclerView = findViewById(R.id.epharmacy_rv)
            fotoResepButton = findViewById(R.id.foto_resep_button)
            doneButton = findViewById(R.id.selesai_button)
        }
    }

    private fun initData(){
        renderToolTip()
        renderFotoResepButton()
        renderDoneButton()
    }

    private fun renderDoneButton() {
        doneButton?.setOnClickListener {
            onClickDoneButton()
        }
    }

    private fun renderToolTip() {
        val terms = getString(R.string.epharmacy_terms)
        val spannableString = SpannableString(terms)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showTnC()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                ds.isUnderlineText = false
                ds.typeface = Typeface.DEFAULT_BOLD
            }
        }
        spannableString.setSpan(clickableSpan, 44, 65, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        ePharmacyToolTipText?.text = spannableString
        ePharmacyToolTipText?.isClickable = true
    }

    private fun renderFotoResepButton() {
        MethodChecker.getDrawable(context,com.tokopedia.iconunify.R.drawable.iconunify_camera)?.let {
            DrawableCompat.setTint(
                DrawableCompat.wrap(it),
                MethodChecker.getColor(context,com.tokopedia.unifyprinciples.R.color.Green_G500)
            )
            fotoResepButton?.setDrawable(it, UnifyButton.DrawablePosition.LEFT)
        }
        fotoResepButton?.setOnClickListener {
            onClickFotoResepButton()
        }
    }

    private fun showTnC() {

    }

    private fun onClickFotoResepButton() {

    }

    private fun onClickDoneButton() {

    }

    override fun getScreenName() = ""

    override fun initInjector() = getComponent(EPharmacyComponent::class.java).inject(this)

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): UploadPrescriptionFragment {
            val fragment = UploadPrescriptionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}