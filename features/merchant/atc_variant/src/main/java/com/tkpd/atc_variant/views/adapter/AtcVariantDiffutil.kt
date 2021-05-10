package com.tkpd.atc_variant.views.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by Yehezkiel on 10/05/21
 */
class AtcVariantDiffutil : DiffUtil.ItemCallback<Visitable<*>>() {
    override fun areItemsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return (oldItem as AtcVariantVisitable).uniqueId() == (newItem as AtcVariantVisitable).uniqueId()
    }

    override fun areContentsTheSame(oldItem: Visitable<*>, newItem: Visitable<*>): Boolean {
        return (oldItem as AtcVariantVisitable).isEqual(newItem as AtcVariantVisitable)
    }

    override fun getChangePayload(oldItem: Visitable<*>, newItem: Visitable<*>): Any? {
        return (oldItem as AtcVariantVisitable).getChangePayload(newItem as AtcVariantVisitable)
    }
}