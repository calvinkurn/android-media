package com.tokopedia.otp.cotp.view.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.otp.R;
import com.tokopedia.otp.cotp.view.viewlistener.SelectVerification;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationMethodAdapter extends RecyclerView.Adapter<VerificationMethodAdapter
        .ViewHolder> {

    private final SelectVerification.View viewListener;
    private ArrayList<MethodItem> list;

    public VerificationMethodAdapter(ArrayList<MethodItem> list, SelectVerification.View viewListener) {
        this.list = list;
        this.viewListener = viewListener;
    }

    public static VerificationMethodAdapter createInstance(SelectVerification.View
                                                                   viewListener) {
        return new VerificationMethodAdapter(new ArrayList<MethodItem>(), viewListener);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView methodText;
        ImageView icon;
        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            methodText = itemView.findViewById(R.id.method_text);
            mainView = itemView.findViewById(R.id.main_view);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.verification_method_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (list.get(position).getIconResId() != 0) {
            ImageHandler.loadImageWithId(holder.icon, list.get(position)
                    .getIconResId());
        } else {
            ImageHandler.LoadImage(holder.icon, list.get(position)
                    .getImageUrl());
        }

        holder.methodText.setText(MethodChecker.fromHtml(list.get(position).getMethodText()));
        holder.mainView.setOnClickListener(v -> viewListener.onMethodSelected(list.get(position)));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<MethodItem> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

}
