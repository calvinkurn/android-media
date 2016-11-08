package com.tokopedia.core.myproduct.view;

import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.R2;
import com.tokopedia.core.myproduct.fragment.AddProductFragment;
import com.tokopedia.core.myproduct.utils.DelegateOnClick;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by noiz354 on 5/13/16.
 */
public class AddProductSubmit {
    DelegateOnClick delegateOnClick;

    @Bind(R2.id.add_product_submit)
    TextView submit;
    @Bind(R2.id.add_product_submit_and_push)
    TextView submitAndAdd;

    public AddProductSubmit(View view){
        ButterKnife.bind(this, view);
    }

    @OnClick(R2.id.add_product_submit)
    public void saveAndPushProduct(){
        if(delegateOnClick != null && delegateOnClick instanceof AddProductFragment){
            ((AddProductFragment)delegateOnClick).pushProduct();
        }
    }

    @OnClick(R2.id.add_product_submit_and_push)
    public void saveProduct(){
        if(delegateOnClick != null && delegateOnClick instanceof AddProductFragment){
            ((AddProductFragment)delegateOnClick).pushAndCreateNewProduct();
        }
    }

    public DelegateOnClick getDelegateOnClick() {
        return delegateOnClick;
    }

    public void setDelegateOnClick(DelegateOnClick delegateOnClick) {
        this.delegateOnClick = delegateOnClick;
    }
}
