package com.tokopedia.core.myproduct.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.PictureDB;
import com.tokopedia.core.myproduct.presenter.ImageGalleryImpl;
import com.tokopedia.core.myproduct.utils.VerificationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by m.normansyah on 18/01/2016.
 */
public class ImageDescriptionDialog  extends DialogFragment {
    @BindView(R2.id.add_prod_img_desc_deskripsi)
    EditText addProdDesc;
    @BindView(R2.id.add_prod_img_desc_cancel)
    Button addProdCancel;
    @BindView(R2.id.add_prod_img_desc_ok)
    Button addProdOk;

    public static final String FRAGMENT_TAG = ImageDescriptionDialog.class.getSimpleName();
    private static final String IMAGE_ID = "IMAGE_ID";
    long imageId = -1;
    PictureDB pictureDB;
    private Unbinder unbinder;

    public static ImageDescriptionDialog newInstance(long imageId){
        ImageDescriptionDialog imageDescriptionDialog = new ImageDescriptionDialog();
        Bundle bundle = new Bundle();
        bundle.putLong(IMAGE_ID, imageId);
        imageDescriptionDialog.setArguments(bundle);
        return imageDescriptionDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle( DialogFragment.STYLE_NO_TITLE, android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.add_product_image_description, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        fetchArgument(getArguments());
        return parentView;
    }

    private void fetchArgument(Bundle bundle){
        if(bundle!=null){
            long imageId = bundle.getLong(IMAGE_ID,-1);
            if(imageId!= -1){
                this.imageId = imageId;
                pictureDB = DbManagerImpl.getInstance().getGambarById(imageId);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(pictureDB !=null){
            addProdDesc.setText(pictureDB.getPictureDescription());
        }
        addProdCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDescriptionDialog.this.dismiss();
            }
        });

        addProdOk.setClickable(true);
        addProdOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProdOk.setClickable(false);
                ImageGalleryImpl.Pair<Boolean, String> res
                        = VerificationUtils
                            .validateDescription(ImageDescriptionDialog.this.getActivity(), addProdDesc.getText().toString());
                if(!res.getModel1()){
                    addProdOk.setClickable(true);
                    addProdDesc.setError(res.getModel2());
                }else{
                    pictureDB.setPictureDescription(addProdDesc.getText().toString());
                    pictureDB.save();
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ImageDescriptionDialog.this.dismiss();
                        }
                    }, 100);
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        unbinder.unbind();
    }
}
