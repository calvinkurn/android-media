package com.tokopedia.loginphone.chooseaccount.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.loginphone.R;
import com.tokopedia.loginphone.chooseaccount.data.UserDetail;
import com.tokopedia.loginphone.chooseaccount.view.listener.ChooseTokocashAccountContract;

import java.util.List;

/**
 * @author by nisie on 12/4/17.
 */

public class TokocashAccountAdapter extends RecyclerView.Adapter<TokocashAccountAdapter.ViewHolder> {

    private ChooseTokocashAccountContract.ViewAdapter viewListener;
    private List<UserDetail> list;
    private String phone;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;

        TextView name;
        TextView email;
        View mainView;
        public ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            mainView = itemView.findViewById(R.id.main_view);

            mainView.setOnClickListener(v -> viewListener.onSelectedTokocashAccount(list.get(getAdapterPosition()),phone));
        }

    }
    public static TokocashAccountAdapter createInstance(ChooseTokocashAccountContract.ViewAdapter viewListener,
                                                        List<UserDetail> listAccount,
                                                        String phone) {
        return new TokocashAccountAdapter(viewListener, listAccount, phone);
    }

    private TokocashAccountAdapter(ChooseTokocashAccountContract.ViewAdapter viewListener, List<UserDetail> listAccount, String phone) {
        this.list = listAccount;
        this.viewListener = viewListener;
        this.phone = phone;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TokocashAccountAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tokocash_account_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageHandler.LoadImage(holder.avatar, list.get(position).getImage());
        holder.name.setText(MethodChecker.fromHtml(list.get(position).getFullname()));
        holder.email.setText(list.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<UserDetail> list, String phone) {
        this.phone = phone;
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }
}
