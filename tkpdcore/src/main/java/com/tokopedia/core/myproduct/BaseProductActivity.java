package com.tokopedia.core.myproduct;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.myproduct.dialog.DialogFragmentImageAddProduct;

/**
 * Created by Toped18 on 7/12/2016.
 */
public abstract class BaseProductActivity extends TActivity {
    public static void showEditImageDialog(FragmentManager fm, int position, boolean isPrimary, DialogFragmentImageAddProduct.DFIAListener dfiaListener){
        DialogFragment dialogFragment  = DialogFragmentImageAddProduct.newInstance(position, isPrimary);
        dialogFragment.show(fm, DialogFragmentImageAddProduct.FRAGMENT_TAG);
        ((DialogFragmentImageAddProduct)dialogFragment).setDfiaListener(dfiaListener);
    }

    public static void showEditImageDialog(FragmentManager fm, int position, int fragmentPosition, boolean isPrimary, DialogFragmentImageAddProduct.DFIAListener dfiaListener){
        DialogFragment dialogFragment  = DialogFragmentImageAddProduct.newInstance(position, fragmentPosition, isPrimary);
        dialogFragment.show(fm, DialogFragmentImageAddProduct.FRAGMENT_TAG);
        ((DialogFragmentImageAddProduct)dialogFragment).setDfiaListener(dfiaListener);
    }
}
