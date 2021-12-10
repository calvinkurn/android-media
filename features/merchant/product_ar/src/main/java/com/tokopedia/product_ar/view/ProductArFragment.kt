package com.tokopedia.product_ar.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.modiface.mfemakeupkit.MFEMakeupEngine
import com.modiface.mfemakeupkit.data.MFEMakeupRenderingParameters
import com.modiface.mfemakeupkit.widgets.MFEMakeupView
import com.tokopedia.product_ar.R
import com.tokopedia.product_ar.model.ProductAr
import com.tokopedia.product_ar.viewmodel.ProductArViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ProductArFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private var viewModel: ProductArViewModel? = null

    private var mMakeupView: MFEMakeupView? = null
    private var mMakeupEngine: MFEMakeupEngine? = null

    private var partialBottomArView: PartialBottomArView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_ar, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as? ProductArActivity)?.component?.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background))
        }

        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductArViewModel::class.java)

        mMakeupEngine = MFEMakeupEngine(requireActivity().applicationContext, MFEMakeupEngine.Region.US)
        mMakeupEngine?.loadResources(requireActivity().applicationContext, null)
        mMakeupEngine?.setMakeupRenderingParameters(MFEMakeupRenderingParameters(false));
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMakeupView = view.findViewById(R.id.main_img)
        mMakeupEngine?.attachMakeupView(mMakeupView)
        partialBottomArView = PartialBottomArView.build(view)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 123)
        } else {
            mMakeupEngine?.startRunningWithCamera(context)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    private fun observeData() {
        viewModel?.productArLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    partialBottomArView?.setupView(it.data.options["1234"] ?: ProductAr())
                    Log.e("datanya", it.data.toString())
                }
                is Fail -> {
                    Log.e("datanya", it.throwable.localizedMessage)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMakeupEngine?.startRunningWithCamera(context)
            } else {
                AlertDialog.Builder(requireContext())
                        .setTitle("Permission Error")
                        .setMessage("The camera permission is needed for live mode. You must click allow to start the live try-on")
                        .show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductArFragment()
    }
}