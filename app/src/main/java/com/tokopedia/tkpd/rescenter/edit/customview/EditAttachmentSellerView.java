package com.tokopedia.tkpd.rescenter.edit.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.R2;
import com.tokopedia.tkpd.product.customview.BaseView;
import com.tokopedia.tkpd.rescenter.edit.customadapter.AttachmentAdapter;
import com.tokopedia.tkpd.rescenter.edit.listener.SellerEditResCenterListener;

import butterknife.Bind;

/**
 * Created on 8/30/16.
 */
public class EditAttachmentSellerView extends BaseView<Object, SellerEditResCenterListener> {

    @Bind(R2.id.view_upload_proof)
    View viewUploadProof;
    @Bind(R2.id.list_upload_proof)
    RecyclerView attachmentRecyclerView;

    public EditAttachmentSellerView(Context context) {
        super(context);
    }

    public EditAttachmentSellerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SellerEditResCenterListener listener) {

    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_create_res_center_attachment_section;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull Object data) {

    }

    public void attachAdapter(AttachmentAdapter attachmentAdapter) {
        LinearLayoutManager horizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        attachmentRecyclerView.setLayoutManager(horizontal);
        attachmentRecyclerView.setAdapter(attachmentAdapter);
    }
}
