package ai.advance.liveness.view.activity

import ai.advance.liveness.view.OnBackListener
import ai.advance.liveness.view.fragment.LivenessErrorFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class LivenessFailedActivity : BaseSimpleActivity() {

    //    private void initViews() {
    //        int lightColor = getResources().getColor(R.color.liveness_color_light);
    //        int accentColor = getResources().getColor(R.color.liveness_accent);
    //        mRootView.setBackgroundColor(getResources().getColor(lightColor == accentColor ? R.color.liveness_camera_bg_light : R.color.liveness_camera_bg));
    //        boolean isSuccess = LivenessResult.isSuccess();
    //        if (isSuccess) {
    //            mImageBitmap = LivenessResult.getLivenessBitmap();
    //            mResultImageView.setImageBitmap(mImageBitmap);
    //        } else {
    //            mTipTextView.setText(LivenessResult.getErrorMsg());
    //            mResultImageView.setImageResource(R.drawable.icon_liveness_fail);
    //        }
    //        mResultTextView.setText(isSuccess ? R.string.liveness_detection_success : R.string.liveness_detection_fail);
    //
    //        mTryAgainView.setOnClickListener(v -> {
    //            if(isSuccess){
    //                String imagePath = saveToFile(mImageBitmap);
    //                if(isFileExists(imagePath)){
    //                    Intent intent = new Intent();
    //                    intent.putExtra("image_result", imagePath);
    //                    setResult(RESULT_OK, intent);
    //                }else{
    //                    setResult(-5);
    //                }
    //            }else{
    //                setResult(IS_LIVENESS_DETECTION_FAIL);
    //            }
    //            finish();
    //        });
    //        mIMediaPlayer = new IMediaPlayer(this);
    //        mIMediaPlayer.doPlay(isSuccess ? R.raw.detection_success : R.raw.detection_failed, false, 0);
    //    }
    //

    fun updateToolbarTitle(strId: Int){
        supportActionBar?.setTitle(strId)
    }

    override fun onBackPressed() {
        setResult(0)
        super.onBackPressed()
        if (fragment != null && fragment is OnBackListener) {
            (fragment as OnBackListener).trackOnBackPressed()
        }
    }

    override fun getNewFragment(): Fragment? {
        val intent = intent
        val bundle = Bundle()
        bundle.putInt("failed_type", intent.getIntExtra("failed_type", -1))
        val fragment = LivenessErrorFragment.newInstance()
        fragment.arguments = bundle

        return fragment
    }
}
