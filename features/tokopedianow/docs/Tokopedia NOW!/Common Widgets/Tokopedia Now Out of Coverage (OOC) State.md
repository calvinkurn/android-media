![image](../../res/tokopedia_now_ooc.png)


<!--left header table-->
| **Type Factory** | `TokoNowEmptyStateOocTypeFactory` |
| --- | --- |
| **View Holder** | `TokoNowEmptyStateOocViewHolder` |
| **UI Model** | `TokoNowEmptyStateOocUiModel` |
| **Listener** | `TokoNowEmptyStateOocListener` |
| **FE** | [Misael Jonathan](https://tokopedia.atlassian.net/wiki/people/60051d42e64c95006fbaad73?ref=confluence) [Yogie Susdyastama Putra](https://tokopedia.atlassian.net/wiki/people/5c6bf2e6f1a05835f933bf30?ref=confluence) [Said Faisal](https://tokopedia.atlassian.net/wiki/people/5e25eee0ee264b0e745862c3?ref=confluence)  |

**How to Use**

1. Add `TokoNowEmptyStateOocListener` implementation into fragment.
2. Add `TokoNowEmptyStateOocTypeFactory` implementation into adapter type factory & override type `TokoNowEmptyStateOocUiModel`.
3. Add `TokoNowEmptyStateOocViewHolder` into `createViewHolder` method.
4. Add `TokoNowEmptyStateOocUiModel` item into adapter.

**Example**



```
class MyTypeFactory(
  private val tokoNowEmptyStateOocListener: TokoNowEmptyStateOocListener
): TokoNowEmptyStateOocTypeFactory {
    ...
    override fun type(uiModel: TokoNowEmptyStateOocUiModel) = TokoNowEmptyStateOocViewHolder.LAYOUT
    ...
    
    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ...
            TokoNowEmptyStateOocViewHolder.LAYOUT -> TokoNowEmptyStateOocViewHolder(view, tokoNowEmptyStateOocListener)
            ...
            else -> super.createViewHolder(view, type)
        }
    }
}

class Fragment(): TokoNowEmptyStateOocListener {
    override fun onRefreshLayoutPage() {
        // refetch all data from gql
    }
    override fun onGetFragmentManager(): FragmentManager {
        // return fragment manager that is needed to show bottom sheet
    }
    override fun onGetEventCategory(): String {
        // return event category for tracking purpose
    }
}
```

