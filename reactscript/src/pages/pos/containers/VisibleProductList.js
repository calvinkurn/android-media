import React, { Component } from 'react'
import { connect } from 'react-redux'
import {
  View,
  FlatList,
  StyleSheet,
  RefreshControl,
  ActivityIndicator,
  TouchableNativeFeedback,
  Image,
  TextInput,
  DeviceEventEmitter,
} from 'react-native'
import { Text } from '../common/TKPText'
import Picker from '../components/pages/product/EtalaseSelectPopUp'
import { fetchProducts, fetchEtalase, pullToRefresh, onEtalaseChange, resetProductList, fetchShopId } from '../actions/index'
import Product from '../components/pages/product/Product'
import SearchContainer from '../containers/SearchContainer'

class VisibleProductList extends Component {
  constructor(props) {
    super(props)
    this.state = { showEtalasePicker: false }
  }

  closePopUp = () => {
    this.setState({ showEtalasePicker: false })
  }

  onPickerChange = (value, index) => {
    const etalaseId = value
    if (etalaseId == this.props.etalases.selected || this.props.products.isFetching) {
      return
    }
    const queryText = this.props.queryText
    this.props.dispatch(onEtalaseChange(etalaseId))
    this.props.dispatch(resetProductList())
    this.props.dispatch(fetchProducts(this.props.shopId, 0, 25, value, null, queryText))
  }

  componentDidMount() {
    const { dispatch } = this.props
    const { start, rows } = this.props.products.pagination
    const { shopId } = this.props
    const queryText = this.props.queryText
    dispatch(fetchShopId())
    dispatch(fetchEtalase(shopId))
    dispatch(fetchProducts(shopId, 0, 25, 0, null, queryText))

    this.paymentSuccessReloadState = DeviceEventEmitter.addListener("clearState", (res) => {
      console.log(res)
      if (res) {
        dispatch(resetProductList())
        dispatch(fetchShopId())
        dispatch(fetchEtalase(shopId))
        dispatch(fetchProducts(shopId, 0, 25, 0, null, queryText))
      }
    })
  }

  componentWillUnmount() {
    this.paymentSuccessReloadState.remove()
  }

  renderProduct = ({ item }) => {
    return <Product product={item} key={item.product_id} />
  }

  loadMore = () => {
    if (this.props.products.isFetching || !this.props.products.canLoadMore) {
      return
    }
    const queryText = this.props.queryText
    const { start, rows } = this.props.products.pagination
    const etalaseId = this.props.etalases.selected
    const { shopId } = this.props
    this.props.dispatch(fetchProducts(shopId, start, rows, etalaseId, null, queryText))
  }

  handleRefresh = () => {
    const { dispatch } = this.props
    const selectedEtalaseId = this.props.etalases.selected
    const queryText = this.props.queryText
    const { shopId } = this.props
    dispatch(pullToRefresh())
    dispatch(fetchProducts(shopId, 0, 25, selectedEtalaseId, null, queryText))
    dispatch(fetchEtalase(shopId))
  }

  render() {
    const products = this.props.products.items
    const fetchInProgress = this.props.products.isFetching
    const etalases = this.props.etalases.items
    const refreshing = this.props.products.refreshing
    const selectedEtalaseId = this.props.etalases.selected
    const selectedEtalase = etalases.filter(e => e.id == selectedEtalaseId)
    const { shopId } = this.props

    return (
      <View style={styles.container}>
        <Picker
          visible={this.state.showEtalasePicker}
          onBackTap={this.closePopUp}
          onChange={this.onPickerChange}
          value={selectedEtalaseId}
          options={etalases} />
        <View style={styles.productListHeader}>
          <View style={{ width: '55%', }}>
            <SearchContainer shopId={shopId} />
          </View>
          <View style={{ flexDirection: 'row', alignItems: 'center', width: '45%', justifyContent: 'flex-end' }}>
            <Text style={styles.etalaseText}>Etalase: </Text>
            <TouchableNativeFeedback onPress={() => { this.setState({ showEtalasePicker: true }) }}>
              <View style={styles.etalasePicker}>
                <Text style={{ fontSize: 14, paddingRight: 50 }}>{selectedEtalase[0].name}</Text>
                <Image source={{ uri: 'https://ecs7.tokopedia.net/img/android_o2o/arrow-down-grey.png' }} />
              </View>
            </TouchableNativeFeedback>
          </View>
        </View>
        {products.length > 0 && <FlatList
          data={products}
          keyExtractor={item => item.product_id}
          renderItem={this.renderProduct}
          onEndReached={this.loadMore}
          onEndReachedThreshold={0.5}
          numColumns={3}
          refreshControl={
            <RefreshControl
              refreshing={refreshing}
              onRefresh={this.handleRefresh}
              title="Pull to refresh"
              colors={['#42b549']}
            />
          }
        />}
        {(fetchInProgress && !refreshing) && <ActivityIndicator size='large' />}
      </View>
    )
  }
}

const mapStateToProps = state => {
  const products = state.products
  const etalases = state.etalase
  const queryText = state.search.query
  const shopId = state.shop.shopId
  return {
    products,
    etalases,
    queryText,
    shopId
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: '90%',
    paddingBottom: 30,
  },
  productListHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 14,
    marginBottom: 14,
    paddingRight: 5
  },
  etalaseText: {
    fontWeight: 'bold',
    fontSize: 18,
  },
  etalasePicker: {
    flexDirection: 'row',
    borderWidth: 1,
    borderColor: '#e0e0e0',
    backgroundColor: '#fff',
    padding: 10,
    alignItems: 'center',
    marginLeft: 20,
  },
})

export default connect(mapStateToProps)(VisibleProductList)