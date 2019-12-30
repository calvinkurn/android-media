package ai.advance.liveness.lib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import ai.advance.common.utils.BitmapUtil;
import ai.advance.common.utils.LogUtil;

class DetectionFrame {

    DetectionFrame(byte[] yuvData, int cameraAngle, int width, int height, Detector.DetectionType detectionType) {
        mDetectionType = detectionType;
        mCameraAngle = cameraAngle;
        mYUVData = yuvData;
        mWidth = width;
        mHeight = height;
        mFaceInfo = null;
    }

    public byte[] getYUVData() {
        return mYUVData;
    }

    public byte[] getBitmapPixels() {
        Bitmap bitmap = getBitmap();//裁剪过的
        mCropHeight = bitmap.getHeight();
        mCropWidth = bitmap.getWidth();
        byte[] argbData = BitmapUtil.getPixelsARGB(bitmap);
        bitmap.recycle();
        return argbData;
    }

    public boolean hasFace() {
        return mFaceInfo != null;
    }

    public FaceInfo getFaceInfo() {
        return mFaceInfo;
    }

    public Detector.DetectionType getDetectionType() {
        return mDetectionType;
    }

    /**
     * get 300*300 bitmap
     *
     * @return
     */
    public Bitmap getBitmap() {
        return getBitmap(EXPECT_SIZE);
    }

    /**
     * get scaled bitmap
     *
     * @return
     */
    public Bitmap getBitmap(int scaleSize) {
        return parseYuvToBitmap(scaleSize);
    }

    public int getHeight() {
        return mCropHeight;
    }

    public int getWidth() {
        return mCropWidth;
    }

    public void update(String faceInfo) {
        mFaceInfo = FaceInfo.FaceInfoFactory.createFaceInfo(faceInfo);
    }

    public void update(JSONObject livenessResult) {


        try {
            mFaceWarnCode = Detector.WarnCode.valueOf(livenessResult.getInt("code"));
            mActionStatus = Detector.ActionStatus.valueOf(livenessResult.getInt("status"));
            if (!livenessResult.getString("faceInfo").equals("")) {
                JSONObject faceInfo;
                faceInfo = new JSONObject(livenessResult.getString("faceInfo"));
                mFaceInfo = FaceInfo.FaceInfoFactory.createFaceInfo(faceInfo);
            }
        } catch (JSONException e) {
            LogUtil.debug("json_error", e.toString());
        }

    }

    @Deprecated
    public boolean isBrightLight() {
        return mBrightness > 1;
    }

    /**
     * 光线过暗
     *
     * @return
     */
    @Deprecated
    public boolean isWeakLight() {
        return mBrightness < 0;
    }

    private byte[] mYUVData;
    private int mWidth;
    private int mHeight;
    private int mCropWidth;
    private int mCropHeight;
    protected FaceInfo mFaceInfo;
    public double mBrightness;
    private Detector.DetectionType mDetectionType;
    public Detector.WarnCode mFaceWarnCode;
    public Detector.ActionStatus mActionStatus;
    private static final int EXPECT_SIZE = 300;
    private int mCameraAngle;

    public Detector.WarnCode getFaceWarnCode() {
        return mFaceWarnCode;
    }

    /**
     * parse yuv data to bitmap
     *
     * @return
     */
    private Bitmap parseYuvToBitmap(int scaleSize) {

        YuvImage localYuvImage = new YuvImage(mYUVData, 17, mWidth, mHeight, null);
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();

        localYuvImage.compressToJpeg(new Rect(mWidth - mHeight, 0, mWidth, mHeight), 80, localByteArrayOutputStream);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = mHeight / scaleSize;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap decodeBitmap = BitmapUtil.decodeRGBStreamToBitmap(localByteArrayOutputStream, options);
        Bitmap rotateBitmap;
        if (GuardianLivenessDetectionSDK.isEmulator) {
            rotateBitmap = decodeBitmap;
        } else {
            rotateBitmap = BitmapUtil.rotateBitmap(decodeBitmap, mCameraAngle - 180);
        }
        Matrix matrix = new Matrix();
        float scale = scaleSize / (float) rotateBitmap.getWidth();
        matrix.setScale(scale, scale);
        Bitmap scaleBitmap = Bitmap.createBitmap(rotateBitmap, 0, 0, rotateBitmap.getWidth(), rotateBitmap.getHeight(), matrix, true);
        // scaleBitmap must be 300px*300px
        rotateBitmap.recycle();
        return scaleBitmap;
    }

    /**
     * parse bitmap to Base64 str
     *
     * @return
     */
    public String getFormatBitmap(int scaleSize) {
        Bitmap bitmap = getBitmap(scaleSize);
        byte[] bytes = bitmap2Bytes(bitmap);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    /**
     * parse  300*300 bitmap  toBase64 str
     *
     * @return
     */
    public String getFormatBitmap() {
        return getFormatBitmap(EXPECT_SIZE);
    }

    private byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

}
