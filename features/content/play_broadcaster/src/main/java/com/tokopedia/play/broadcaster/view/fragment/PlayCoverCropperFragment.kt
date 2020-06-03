package com.tokopedia.play.broadcaster.view.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseSetupFragment
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_play_cover_crop.*

/**
 * @author by furqan on 03/06/2020
 */
class PlayCoverCropperFragment : PlayBaseSetupFragment() {

    override fun getScreenName(): String = "Play Cover Title Setup"

    override fun refresh() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            arguments?.let {
                if (!it.containsKey(EXTRA_IMAGE_URI) && !it.containsKey(EXTRA_IMAGE_URL)) {
                    // back
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_cover_crop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeLiveData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }

    private fun initView() {

    }

    private fun setupView() {
        bottomSheetCoordinator.setupTitle(getString(R.string.play_prepare_cover_title_title))

        ivPlayCoverCropImage.setImageURI(Uri.parse(arguments?.getString(EXTRA_IMAGE_URI)))
    }

    private fun observeLiveData() {
    }

    companion object {
        const val EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL"
        const val EXTRA_IMAGE_URI = "EXTRA_IMAGE_URI"
    }

}