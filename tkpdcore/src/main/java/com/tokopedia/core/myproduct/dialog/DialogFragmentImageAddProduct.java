package com.tokopedia.core.myproduct.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.tokopedia.core.R;

/**
 * Created by Toped18 on 5/30/2016.
 */
public class DialogFragmentImageAddProduct extends DialogFragment {

    public static final String FRAGMENT_TAG = DialogFragmentImageAddProduct.class.getSimpleName();
    public static final String IMAGE_PRODUCT_POSITION = "IMAGE_PRODUCT_POSITION";
    private static final String TAG = "STUART";
    public static final String IMAGE_IS_PRIMARY = "IMAGE_IS_PRIMARY";
    public static final String FRAGMENT_PRODUCT_POSITION = "FRAGMENT_PRODUCT_POSITION";
    private CharSequence imageMenu[];
    private DFIAListener dfiaListener;

    public static final int DELETE_IMAGE = 0;
    public static final int CHANGE_IMAGE = 1;
    @Deprecated
    public static final int PRIMARY_IMAGE = 4;
    public static final int ADD_DESCRIPTION = 2;
    public static final int CHANGE_TO_PRIMARY = 3;

    public int position;
    public int fragmentPosition;
    public boolean isPrimary;

    public static DialogFragment newInstance(int position, boolean isPrimary){

        Log.i(TAG,"Creating dialog image editor for add product");

        DialogFragmentImageAddProduct f = new DialogFragmentImageAddProduct();
        Bundle args = new Bundle();
        args.putInt(IMAGE_PRODUCT_POSITION, position);
        args.putBoolean(IMAGE_IS_PRIMARY, isPrimary);
        f.setArguments(args);
        return f;
    }

    public static DialogFragment newInstance(int position, int fragmentPosition, boolean isPrimary){
        Log.i(TAG,"Creating dialog image editor for instoped");

        DialogFragmentImageAddProduct f = new DialogFragmentImageAddProduct();
        Bundle args = new Bundle();
        args.putInt(IMAGE_PRODUCT_POSITION, position);
        args.putInt(FRAGMENT_PRODUCT_POSITION,fragmentPosition);
        args.putBoolean(IMAGE_IS_PRIMARY, isPrimary);
        f.setArguments(args);
        return f;
    }

    public interface DFIAListener{
        void editImage(int action, int position, int fragmentPosition, boolean isPrimary);
    }

    public void setDfiaListener(DFIAListener dfiaListener) {
        this.dfiaListener = dfiaListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        position = getArguments().getInt(IMAGE_PRODUCT_POSITION);
        isPrimary = getArguments().getBoolean(IMAGE_IS_PRIMARY);
        fragmentPosition = getArguments().getInt(FRAGMENT_PRODUCT_POSITION);
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (isPrimary) {
            imageMenu = new CharSequence[]{
                    getActivity().getString(R.string.title_img_delete),
                    getActivity().getString(R.string.action_edit),
                    getActivity().getString(R.string.title_img_desc)};
        } else {
            imageMenu = new CharSequence[]{
                    getActivity().getString(R.string.title_img_delete),
                    getActivity().getString(R.string.action_edit),
                    getActivity().getString(R.string.title_img_desc),
                    getActivity().getString(R.string.title_img_default)};
        }
        builder.setItems(imageMenu,getImageAddProductListener());
        return builder.create();
    }

    private DialogInterface.OnClickListener getImageAddProductListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dfiaListener != null){
                    dfiaListener.editImage(which, position, fragmentPosition, isPrimary);
                }
            }
        };
    }
}
