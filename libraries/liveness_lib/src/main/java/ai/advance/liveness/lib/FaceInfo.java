package ai.advance.liveness.lib;


import android.graphics.PointF;
import android.graphics.RectF;

import org.json.JSONArray;
import org.json.JSONObject;

class FaceInfo {
    private RectF position;
    private float yaw = 0.0F;
    float faceQuality = 0.0F;
    private float mouthOpenProb = -1.0F;
    private float leftEyeOpenProb = -1.0F;
    private float rightEyeOpenProb = -1.0F;
    private PointF[] landmark; //
    boolean faceTooSmall = false;

    FaceInfo() {
        position = new RectF();
    }

    public String toString() {
        return "FaceInfo{ position=" + this.position.toShortString() +
                ", yaw=" + this.yaw +
                ", faceQuality=" + this.faceQuality +
                ", mouthOpenProb=" + this.mouthOpenProb + "}";
    }

    static class FaceInfoFactory {
        FaceInfoFactory() {
        }

        static FaceInfo createFaceInfo(String faceInfoString) {
            try {
                JSONObject faceInfoJson = new JSONObject(faceInfoString);
                return createFaceInfo(faceInfoJson);
            } catch (Exception exception) {
                // TODO: check exception handle
                return null;
            }
        }

        static FaceInfo createFaceInfo(JSONObject faceInfoJson) {
            try {
                FaceInfo faceInfo = new FaceInfo();
                JSONArray array;
                faceInfo.leftEyeOpenProb = (float) faceInfoJson.optDouble("leftEyeOpenProb");
                faceInfo.rightEyeOpenProb = (float) faceInfoJson.optDouble("rightEyeOpenProb");
                faceInfo.mouthOpenProb = (float) faceInfoJson.optDouble("mouthOpenProb");
                faceInfo.faceQuality = (float) faceInfoJson.optDouble("faceQuality");
                faceInfo.yaw = (float) faceInfoJson.optDouble("yaw");
                array = faceInfoJson.optJSONArray("faceBoundingbox");
                faceInfo.position.left = (float) array.optDouble(0);
                faceInfo.position.top = (float) array.optDouble(1);
                faceInfo.position.right = (float) array.optDouble(2);
                faceInfo.position.bottom = (float) array.optDouble(3);
                array = faceInfoJson.optJSONArray("landmark");
                if (array.length() % 2 == 0) {
                    faceInfo.landmark = new PointF[array.length() / 2];
                    int j = 0;
                    for (int i = 0; i < array.length() / 2; ++i) {
                        faceInfo.landmark[i] = new PointF();
                        faceInfo.landmark[i].x = (float) array.optDouble(j);
                        j++;
                        faceInfo.landmark[i].y = (float) array.optDouble(j);
                        j++;
                    }
                }
                return faceInfo;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
