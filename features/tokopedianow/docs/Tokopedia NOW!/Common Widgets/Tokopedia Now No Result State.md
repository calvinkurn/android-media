![image](../../res/tokopedia_now_no_result_state.png)

<!--left header table-->
| **Type Factory** | `TokoNowEmptyStateNoResultTypeFactory` |
| --- | --- |
| **View Holder** | `TokoNowEmptyStateNoResultViewHolder` |
| **UI Model** | `TokoNowEmptyStateNoResultUiModel` |
| **Listener** | `TokoNowEmptyStateNoResultListener` |
| **FE** | [Darian Thedy](https://tokopedia.atlassian.net/wiki/people/5c94aa568c3aae2d15117504?ref=confluence)  |

**How to Use**

1. Add `TokoNowEmptyStateNoResultListener` implementation into fragment.
2. Add `TokoNowEmptyStateNoResultTypeFactory` implementation into adapter type factory & override type `TokoNowEmptyStateNoResultUiModel`.
3. Add `TokoNowEmptyStateNoResultViewHolder` into `createViewHolder` method.
4. Add `TokoNowEmptyStateNoResultUiModel` item into adapter.

**Example**



```
class MyTypeFactory(
  private val tokoNowEmptyStateNoResultListener: TokoNowEmptyStateNoResultListener
): TokoNowEmptyStateNoResultTypeFactory {
    ...
    override fun type(uiModel: TokoNowEmptyStateNoResultUiModel) = TokoNowEmptyStateNoResultViewHolder.LAYOUT
    ...
    
    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ...
            TokoNowEmptyStateNoResultViewHolder.LAYOUT -> TokoNowEmptyStateNoResultViewHolder(view, tokoNowEmptyStateNoResultListener)
            ...
            else -> super.createViewHolder(view, type)
        }
    }
}

class Fragment(): TokoNowEmptyStateNoResultListener {
    override fun onFindInTokopediaClick() {
        // back to tokopedia home page
    }

    override fun goToTokopediaNowHome() {
        // back to TokoNow home page
    }

    override fun onRemoveFilterClick(option: Option) {
        // remove option of filter
    }
}
```

