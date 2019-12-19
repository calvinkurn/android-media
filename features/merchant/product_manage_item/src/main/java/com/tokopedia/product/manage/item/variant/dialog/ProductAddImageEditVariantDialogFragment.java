package com.tokopedia.product.manage.item.variant.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import com.tokopedia.product.manage.item.R;


/**
 * Created by Toped18 on 5/30/2016.
 */
public class ProductAddImageEditVariantDialogFragment extends DialogFragment {

    public static final String FRAGMENT_TAG = ProductAddImageEditVariantDialogFragment.class.getSimpleName();

    private CharSequence imageMenu[];
    private OnImageEditListener mListener;

    public interface OnImageEditListener {
        void clickChangeImagePath();

        void clickImageEditor();

        void clickRemoveImage();
    }

    public static ProductAddImageEditVariantDialogFragment newInstance() {
        return new ProductAddImageEditVariantDialogFragment();
    }

    public void setOnImageEditListener(OnImageEditListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        imageMenu = new CharSequence[]{
                getString(R.string.title_img_delete),
                getString(R.string.change_image),
                getString(R.string.action_editor)};
        builder.setItems(imageMenu, getImageAddProductListener());
        return builder.create();
    }

    private DialogInterface.OnClickListener getImageAddProductListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    CharSequence stringClicked = imageMenu[which];
                    if (stringClicked.equals(getString(R.string.title_img_delete))) {
                        mListener.clickRemoveImage();
                    } else if (stringClicked.equals(getString(R.string.change_image))) {
                        mListener.clickChangeImagePath();
                    } else if (stringClicked.equals(getString(R.string.action_editor))) {
                        mListener.clickImageEditor();
                    }
                }
            }
        };
    }
}
