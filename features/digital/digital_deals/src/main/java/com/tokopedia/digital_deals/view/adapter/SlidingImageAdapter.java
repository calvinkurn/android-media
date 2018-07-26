package com.tokopedia.digital_deals.view.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;


import java.util.List;



public class SlidingImageAdapter extends PagerAdapter {


    private List<String> imagesUrl;
    private Context context;
    private DealsHomePresenter mPresenter;


    public SlidingImageAdapter(List<String> imagesUrl, DealsHomePresenter presenter) {
        this.imagesUrl = imagesUrl;
        this.mPresenter = presenter;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return (imagesUrl == null) ? 0 : imagesUrl.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        this.context=view.getContext();
        View imageLayout = LayoutInflater.from(context).inflate(R.layout.deals_banner_item, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.banner_item);

        ImageHandler.loadImage(context, imageView, imagesUrl.get(position), R.color.grey_1100, R.color.grey_1100);

        view.addView(imageLayout, 0);
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onClickBanner();
            }
        });
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);

    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
