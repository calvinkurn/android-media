package com.tokopedia.core.myproduct.view;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.myproduct.ProductSocMedActivity;
import com.tokopedia.core.myproduct.fragment.AddProductFragment;
import com.tokopedia.core.myproduct.utils.AddProductType;
import com.tokopedia.core.myproduct.utils.DelegateOnClick;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by noiz354 on 5/13/16.
 */
public class AddProductSocMedSubmit {
    DelegateOnClick delegateOnClick;

    @Bind(R2.id.add_product_soc_med_submit)
    TextView add;
    @Bind(R2.id.add_product_soc_med_delete)
    TextView delete;

    public AddProductSocMedSubmit(View view){
        ButterKnife.bind(this, view);
    }

    public DelegateOnClick getDelegateOnClick() {
        return delegateOnClick;
    }

    public void setDelegateOnClick(DelegateOnClick delegateOnClick) {
        this.delegateOnClick = delegateOnClick;
    }

    @OnClick(R2.id.add_product_soc_med_submit)
    public void submit(){
        if(delegateOnClick != null && delegateOnClick instanceof AddProductFragment){
            if(((AddProductFragment)delegateOnClick).addProductType == AddProductType.EDIT){
                ((AddProductFragment) delegateOnClick).editProduct(true);
            }else {
                ((AddProductFragment) delegateOnClick).pushProduct();
            }
        }
    }

    @OnClick(R2.id.add_product_soc_med_delete)
    public void delete(){
        if(delegateOnClick != null && delegateOnClick instanceof AddProductFragment){
            AddProductFragment delegateOnClick = (AddProductFragment) this.delegateOnClick;
            if(delegateOnClick.getActivity() instanceof  ProductSocMedActivity) {
                delegateOnClick.removeFragment(
                        ((ProductSocMedActivity) delegateOnClick.getActivity()).getCurrentFragmentPosition()
                );
            }
            delegateOnClick.deleteProductDialog();

        }
    }

    public void turnOffButton(){
        add.setOnClickListener(null);
        delete.setOnClickListener(null);
        add.setBackgroundResource(R.color.tkpd_dark_gray);
        delete.setBackgroundResource(R.color.tkpd_dark_gray);
    }


}
