package com.tokopedia.tkpd.rescenter.create.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.rescenter.create.customadapter.AttachmentAdapter;
import com.tokopedia.tkpd.rescenter.create.listener.ChooseSolutionListener;
import com.tokopedia.tkpd.rescenter.create.model.passdata.ActionParameterPassData;

import butterknife.Bind;

/**
 * Created on 7/5/16.
 */
public class AttachmentSectionCreateResCenterView extends BaseView<ActionParameterPassData, ChooseSolutionListener>{

    @Bind(R.id.view_upload_proof)
    View viewUploadProof;
    @Bind(R.id.list_upload_proof)
    RecyclerView attachmentRecyclerView;

    public AttachmentSectionCreateResCenterView(Context context) {
        super(context);
    }

    public AttachmentSectionCreateResCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        setVisibility(GONE);
    }

    @Override
    public void setListener(ChooseSolutionListener chooseSolutionListener) {
        this.listener = chooseSolutionListener;
    }

    @Override
    public void renderData(@NonNull ActionParameterPassData data) {

    }

    public void attachAdapter(AttachmentAdapter attachmentAdapter) {
        LinearLayoutManager horizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        attachmentRecyclerView.setLayoutManager(horizontal);
        attachmentRecyclerView.setAdapter(attachmentAdapter);
    }
}
