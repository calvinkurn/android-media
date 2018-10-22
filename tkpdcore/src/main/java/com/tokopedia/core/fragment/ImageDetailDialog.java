package com.tokopedia.core.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.core2.R;
import com.tokopedia.core.customwidget.TouchImageView;

import java.io.IOException;

/**
 * Created by Kris on 6/15/2015.
 */
public class ImageDetailDialog extends DialogFragment{

    private Context context;
    private String imageName;
    private TextView zoomInstruction;
    private TouchImageView focusImage;
    private ImageView closeButton;
    boolean imageTouched;

    public ImageDetailDialog(){

    }
    public static ImageDetailDialog openImageFromAsset(Context context, String imageName){
        ImageDetailDialog imageDialog = new ImageDetailDialog();
        imageDialog.context = context;
        imageDialog.imageName = imageName;
        imageDialog.imageTouched = false;
        return imageDialog;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_detail, container, false);
        focusImage = (TouchImageView) view.findViewById(R.id.examined_image);
        closeButton = (ImageView) view.findViewById(R.id.close_button_klikbca_detail);
        closeButton.setOnClickListener(closeDialog());
        setImageBitmap();
        return view;
    }
    private void setImageBitmap(){
        try {
            focusImage.setImageBitmap(BitmapFactory.decodeStream(context.getAssets().open(imageName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private View.OnTouchListener imageListener(){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        };
    }
    private TouchImageView.OnStateChange imageStateChange(){
        return new TouchImageView.OnStateChange() {
            @Override
            public void OnStateChanged(float StateSize) {

            }
        };
    }
    private View.OnClickListener closeDialog(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };
    }

}
