![image](../../res/tokopedia_now_repurchase_widget.png)

<!--left header table-->
| **Type Factory** | `TokoNowRepurchaseTypeFactory` |
| --- | --- |
| **View Holder** | `TokoNowRepurchaseViewHolder` |
| **UI Model** | `TokoNowRepurchaseUiModel` |
| **Listener** | `TokoNowProductCardListener` |
| **Use Case** | `GetRepurchaseWidgetUseCase` |
| **GQL** | [GQL Repurchase Widget API](/wiki/spaces/TokoNow/pages/1603799887/GQL+Repurchase+Widget+API)  |
| **FE** | [Reza Gama Hidayat](https://tokopedia.atlassian.net/wiki/people/5def15952702bc0ec7e775c5?ref=confluence) [Yogie Susdyastama Putra](https://tokopedia.atlassian.net/wiki/people/5c6bf2e6f1a05835f933bf30?ref=confluence)  |
| **BE** | [Muhamad Huda](https://tokopedia.atlassian.net/wiki/people/5c131b12128c7106f576c8a4?ref=confluence) [Rizky Andre Wibisono](https://tokopedia.atlassian.net/wiki/people/612c2a4f0f8ff40068adae28?ref=confluence) [Tommy Wijaya](https://tokopedia.atlassian.net/wiki/people/611c9137aee32f006f98f389?ref=confluence)  |

**How to Use**

1. Add `TokoNowProductCardListener` implementation into fragment.
2. Add `TokoNowTypeFactory` implementation into adapter type factory & override type `TokoNowRecentPurchaseUiModel`.
3. Add `TokoNowRecentPurchaseViewHolder` into `createViewHolder` method.
4. Add `TokoNowRecentPurchaseUiModel` item into adapter.

**Example**



```
class MyTypeFactory(
  private val tokoNowProductCardListener: TokoNowProductCardListener
): TokoNowRepurchaseTypeFactory {
    ...
    override fun type(uiModel: TokoNowRepurchaseUiModel) = TokoNowRepurchaseViewHolder.LAYOUT
    ...
    
    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ...
            TokoNowRepurchaseViewHolder.LAYOUT -> TokoNowRepurchaseViewHolder(view, tokoNowProductCardListener)
            ...
            else -> super.createViewHolder(view, type)
        }
    }
}

class Fragment(): TokoNowProductCardListener {
    override fun onProductQuantityChanged(data: TokoNowProductCardUiModel, quantity: Int) {
        // Add to cart implementation
    }
    
    override fun onProductCardImpressed(data: TokoNowProductCardUiModel) {
        // Add impression tracker here
    }

    override fun onProductCardClicked(position: Int, data: TokoNowProductCardUiModel) {
        // Click product card implementation
    }

    override fun onAddVariantClicked(data: TokoNowProductCardUiModel) {
        // Click add variant implementation
    }
}
```

