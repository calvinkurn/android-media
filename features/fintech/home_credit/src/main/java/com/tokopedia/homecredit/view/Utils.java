package com.tokopedia.homecredit.view;

import android.hardware.Camera;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;

public class Utils {
    public static boolean isFrontCameraAvailable(){
        int numCameras= Camera.getNumberOfCameras();
        for(int i=0;i<numCameras;i++){
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if(CAMERA_FACING_FRONT == info.facing){
                return true;
            }
        }
        return false;
    }
}
