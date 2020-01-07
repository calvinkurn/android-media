package ai.advance.liveness.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tokopedia.imagepicker.common.util.ImageUtils;

import java.io.File;

import ai.advance.common.IMediaPlayer;
import ai.advance.liveness.R;
import ai.advance.liveness.lib.LivenessResult;

public class ResultActivity extends AppCompatActivity {
    private ImageView mResultImageView;
    private TextView mResultTextView;
    private TextView mTipTextView;
    private View mTryAgainView;
    private View mRootView;
    private IMediaPlayer mIMediaPlayer;
    private Bitmap mImageBitmap;
    private int IS_LIVENESS_DETECTION_FAIL = -9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        findViews();
        initViews();
    }

    private void findViews() {
        mResultImageView = findViewById(R.id.detection_result_image_view);
        mResultTextView = findViewById(R.id.detection_result_text_view);
        mTryAgainView = findViewById(R.id.try_again_view);
        mRootView = findViewById(R.id.root_view_activity_result);
        mTipTextView = findViewById(R.id.detection_tip_text_view);
    }

    private void initViews() {
        int lightColor = getResources().getColor(R.color.liveness_color_light);
        int accentColor = getResources().getColor(R.color.liveness_accent);
        mRootView.setBackgroundColor(getResources().getColor(lightColor == accentColor ? R.color.liveness_camera_bg_light : R.color.liveness_camera_bg));
        boolean isSuccess = LivenessResult.isSuccess();
        if (isSuccess) {
            mImageBitmap = LivenessResult.getLivenessBitmap();
            mResultImageView.setImageBitmap(mImageBitmap);
        } else {
            mTipTextView.setText(LivenessResult.getErrorMsg());
            mResultImageView.setImageResource(R.drawable.icon_liveness_fail);
        }
        mResultTextView.setText(isSuccess ? R.string.liveness_detection_success : R.string.liveness_detection_fail);

        mTryAgainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSuccess){
                    String imagePath = saveToFile(mImageBitmap);
                    if(isFileExists(imagePath)){
                        Intent intent = new Intent();
                        intent.putExtra("image_result", imagePath);
                        setResult(RESULT_OK, intent);
                    }else{
                        setResult(-5);
                    }
                }else{
                    setResult(IS_LIVENESS_DETECTION_FAIL);
                }
                finish();
            }
        });
        mIMediaPlayer = new IMediaPlayer(this);
        mIMediaPlayer.doPlay(isSuccess ? R.raw.detection_success : R.raw.detection_failed, false, 0);
    }

    public String saveToFile(Bitmap mImageBitmap) {
        try {
            File cameraResultFile = ImageUtils.writeImageToTkpdPath(ImageUtils
                    .DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE_CAMERA, mImageBitmap, false);
            if (cameraResultFile.exists()) {
                return cameraResultFile.getAbsolutePath();
            } else {
                Toast.makeText(this, "Terjadi kesalahan, silahkan coba lagi", Toast
                        .LENGTH_LONG).show();
            }
        } catch (Throwable error) {
            Toast.makeText(this, "Terjadi kesalahan, silahkan coba lagi", Toast
                    .LENGTH_LONG).show();
        }

        return "";
    }

    private boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    @Override
    protected void onDestroy() {
        if (mIMediaPlayer != null) {
            mIMediaPlayer.close();
        }
        super.onDestroy();
    }
}
