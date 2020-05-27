package com.tokopedia.play.broadcaster.view.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.di.DaggerPlayBroadcasterComponent
import com.tokopedia.play.broadcaster.view.contract.PlayBroadcastCoordinator
import com.tokopedia.play.broadcaster.view.fragment.PlayEtalasePickerFragment
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEtalasePickerViewModel
import javax.inject.Inject

/**
 * Created by jegul on 26/05/20
 */
class PlayBroadcastSetupBottomSheet : BottomSheetDialogFragment(), PlayBroadcastCoordinator {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: PlayEtalasePickerViewModel

    private lateinit var flFragment: FrameLayout
    private lateinit var ivBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var clContent: ConstraintLayout
    private lateinit var flOverlay: FrameLayout

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
            override fun onBackPressed() {
                Toast.makeText(requireContext(), "Back", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayEtalasePickerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_play_broadcast_setup, container, false)
        dialog?.let { setupDialog(it) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun inject() {
        DaggerPlayBroadcasterComponent.create()
                .inject(this)
    }

    private fun setupDialog(dialog: Dialog) {
        dialog.setOnShowListener {
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams = bottomSheet?.layoutParams?.apply {
                height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

            bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {

                }

                override fun onStateChanged(p0: View, p1: Int) {
                    bottomSheetBehavior.peekHeight = bottomSheet?.height ?: maxHeight()
                }
            })
            bottomSheetBehavior.isHideable = false
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun initView(view: View) {
        with(view) {
            flFragment = findViewById(R.id.fl_fragment)
            ivBack = findViewById(R.id.iv_back)
            tvTitle = findViewById(R.id.tv_title)
            clContent = findViewById(R.id.cl_content)
            flOverlay = findViewById(R.id.fl_overlay)
        }
    }

    private fun setupView(view: View) {
        flOverlay.setOnClickListener {
            Toast.makeText(requireContext(), "Overlay", Toast.LENGTH_SHORT).show()
        }

        navigateToFragment(PlayEtalasePickerFragment.newInstance())
    }

    override fun navigateToFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
                .replace(R.id.fl_fragment, fragment)
                .commit()

        if (fragment is PlayBaseSetupFragment) {
            setupTitle(fragment.getTitle())
            ivBack.setImageResource(
                    if (fragment.isRootFragment()) com.tokopedia.unifycomponents.R.drawable.unify_bottomsheet_close
                    else R.drawable.ic_system_action_back_grayscale_24
            )
        } else {
            setupTitle("")
            ivBack.setImageResource(com.tokopedia.unifycomponents.R.drawable.unify_bottomsheet_close)
        }
    }

    override fun setupTitle(title: String) {
        tvTitle.text = title
    }

    private fun maxHeight(): Int = (getScreenHeight() * MAX_HEIGHT_MULTIPLIER).toInt()

    companion object {
        private const val TAG = "PlayBroadcastSetupBottomSheet"

        private const val MAX_HEIGHT_MULTIPLIER = 0.8

        fun newInstance(): PlayBroadcastSetupBottomSheet = PlayBroadcastSetupBottomSheet()
    }
}