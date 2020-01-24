package com.tokopedia.loginphone.chooseaccount.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.loginphone.R;
import com.tokopedia.loginphone.chooseaccount.data.ShopDetail;
import com.tokopedia.loginphone.chooseaccount.data.UserDetail;
import com.tokopedia.loginphone.chooseaccount.view.listener.ChooseAccountContract;
import com.tokopedia.unifyprinciples.Typography;

import java.util.List;

/**
 * @author by nisie on 12/4/17.
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private ChooseAccountContract.ViewAdapter viewListener;
    private List<UserDetail> list;
    private String phone;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        Typography name;
        Typography email;
        Typography shopName;
        View shopView;
        View mainView;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            shopName = itemView.findViewById(R.id.shop_name);
            mainView = itemView.findViewById(R.id.main_view);
            shopView = itemView.findViewById(R.id.shop_view);

            mainView.setOnClickListener(v -> viewListener.onSelectedAccount(list.get(getAdapterPosition()), phone));
        }

    }

    public static AccountAdapter createInstance(ChooseAccountContract.ViewAdapter viewListener,
                                                List<UserDetail> listAccount,
                                                String phone) {
        return new AccountAdapter(viewListener, listAccount, phone);
    }

    private AccountAdapter(ChooseAccountContract.ViewAdapter viewListener, List<UserDetail> listAccount, String phone) {
        this.list = listAccount;
        this.viewListener = viewListener;
        this.phone = phone;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new AccountAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choose_login_phone_account_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserDetail userDetail = list.get(position);
        ImageHandler.loadImageCircle2(context, holder.avatar, userDetail.getImage());
        holder.name.setText(MethodChecker.fromHtml(userDetail.getFullname()));
        String email = userDetail.getEmail();
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email = hideEmail(email);
        }
        holder.email.setText(email);
        ShopDetail shopDetail = userDetail.getShopDetail();
        if(shopDetail != null) {
            if(!shopDetail.getName().isEmpty()){
                holder.shopView.setVisibility(View.VISIBLE);
                holder.shopName.setText(userDetail.getShopDetail().getName());
            }
        }
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

    private String hideEmail(String email) {
        return email.replaceAll("(?<=.{3}).(?=[^@]*?.@)", "*");
    }
}
