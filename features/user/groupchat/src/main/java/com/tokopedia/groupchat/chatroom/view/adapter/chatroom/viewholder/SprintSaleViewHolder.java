package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder;

import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.groupchat.R;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.SprintSaleAdapter;
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleAnnouncementViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleProductViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.SprintSaleViewModel;
import com.tokopedia.groupchat.common.design.SpaceItemDecoration;

import java.util.ArrayList;

/**
 * @author by nisie on 3/22/18.
 */

public class SprintSaleViewHolder extends BaseChatViewHolder<SprintSaleAnnouncementViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.sprint_sale_holder;
    private static final int SPAN_COUNT = 2;
    private final ChatroomContract.ChatItem.SprintSaleViewHolderListener listener;

    private ImageView sprintSaleIcon;
    private TextView sprintSaleTitle;
    private RecyclerView listProducts;
    private SprintSaleAdapter productAdapter;
    private SpaceItemDecoration itemDecoration;
    private View mainLayout;

    public SprintSaleViewHolder(View itemView, ChatroomContract.ChatItem.SprintSaleViewHolderListener listener) {
        super(itemView);
        this.listener = listener;
        mainLayout = itemView.findViewById(R.id.main_layout);
        sprintSaleIcon = itemView.findViewById(R.id.sprintsale_icon);
        sprintSaleTitle = itemView.findViewById(R.id.sprintsale_title);
        productAdapter = SprintSaleAdapter.createInstance(listener);
        itemDecoration = new SpaceItemDecoration((int) itemView.getContext().getResources()
                .getDimension(R.dimen.space_mini), 2);
        listProducts = itemView.findViewById(R.id.list_product);
        listProducts.addItemDecoration(itemDecoration);
        listProducts.setLayoutManager(new GridLayoutManager(itemView.getContext(), SPAN_COUNT,
                LinearLayoutManager.VERTICAL, false));
        listProducts.setAdapter(productAdapter);
    }

    @Override
    public void bind(final SprintSaleAnnouncementViewModel element) {
        super.bind(element);

        checkSprintSaleActive(element.getSprintSaleType());
        setProducts(element.getListProducts());

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSprintSaleComponentClicked(element);
            }
        });
    }

    private void setProducts(ArrayList<SprintSaleProductViewModel> listProducts) {
        productAdapter.setList(listProducts);
    }

    private void checkSprintSaleActive(String sprintSaleType) {
        switch (sprintSaleType.toLowerCase()) {
            case SprintSaleViewModel.TYPE_ACTIVE:
                setSprintSaleActive();
                break;
            case SprintSaleViewModel.TYPE_FINISHED:
                setSprintSaleFinished();
                break;
        }
    }

    private void setSprintSaleActive() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(sprintSaleIcon, R.drawable
                    .ic_sprint_sale_active);
        } else {
            sprintSaleIcon.setImageResource(R.drawable.ic_sprint_sale_active);
        }
        sprintSaleTitle.setText(R.string.title_sprintsale_started);
        sprintSaleTitle.setTextColor(MethodChecker.getColor(sprintSaleTitle.getContext(), R.color.medium_green));
    }

    private void setSprintSaleFinished() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ImageHandler.loadImageWithIdWithoutPlaceholder(sprintSaleIcon, R.drawable
                    .ic_sprint_sale_inactive);
        } else {
            sprintSaleIcon.setImageResource(R.drawable.ic_sprint_sale_inactive);
        }

        sprintSaleTitle.setText(R.string.title_sprintsale_finished);
        sprintSaleTitle.setTextColor(MethodChecker.getColor(sprintSaleTitle.getContext(), R.color.black_54));
    }

}