package com.tokopedia.digital.product.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @author anggaprasetiyo on 5/8/17.
 */
public abstract class BaseDigitalChooserView<T> extends RelativeLayout {
    protected List<T> dataList;
    protected T dataSelected;
    protected ActionListener<T> actionListener;

    public BaseDigitalChooserView(Context context) {
        super(context);
        initialView(context, null, 0);
    }

    public BaseDigitalChooserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialView(context, attrs, 0);
    }

    public BaseDigitalChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialView(context, null, defStyleAttr);
    }

    private void initialView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(getHolderLayoutId(), this, true);
        ButterKnife.bind(this);
        initialViewListener();
    }

    public void setActionListener(ActionListener<T> actionListener) {
        this.actionListener = actionListener;
    }

    protected abstract void initialViewListener();

    protected abstract int getHolderLayoutId();

    public abstract void setLabelText(String labelText);

    public abstract void enableError(String errorMessage);

    public abstract void disableError();

    public abstract void renderInitDataList(List<T> data);

    public abstract void renderUpdateDataSelected(T data);

    protected OnClickListener getOnChooserClickedListener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onDigitalChooserClicked(dataList);
            }
        };
    }

    public interface ActionListener<Z> {
        void onInitialDataDigitalChooserSelectedRendered(Z data);

        void onUpdateDataDigitalChooserSelectedRendered(Z data);

        void onDigitalChooserClicked(List<Z> data);

    }
}
