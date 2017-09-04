import React, { Component } from 'react'
import { connect } from 'react-redux'
import {
  View,
  FlatList,
  StyleSheet,
  RefreshControl,
  ActivityIndicator,
  Text,
} from 'react-native'
import StorefrontPicker from '../components/StorefrontPicker'
import { fetchProducts, fetchEtalase, pullToRefresh, onEtalaseChange, resetProductList } from '../actions/index'
import Product from '../components/Product'

class VisibleProductList extends Component {
  constructor(props) {
    super(props)
  }

  onPickerChange = (value, index) => {
    const etalaseId = value
    this.props.dispatch(onEtalaseChange(etalaseId))
    this.props.dispatch(resetProductList())
    this.props.dispatch(fetchProducts(1987772, 0, 25, value))
  }

  componentDidMount() {
    const { dispatch } = this.props
    const { start, rows } = this.props.products.pagination
    dispatch(fetchEtalase(1987772))
  }

  renderProduct = ({ item }) => {
    return <Product product={item} key={item.id} />
  }

  loadMore = () => {
    if (this.props.products.isFetching || !this.props.products.canLoadMore) {
      return
    }
    const { start, rows } = this.props.products.pagination
    const etalaseId = this.props.etalases.selected
    this.props.dispatch(fetchProducts(1987772, start, rows, etalaseId))
  }

  handleRefresh = () => {
    const { dispatch } = this.props
    const selectedEtalase = this.props.etalases.selected

    dispatch(pullToRefresh())
    dispatch(fetchProducts(1987772, 0, 25, selectedEtalase))
    dispatch(fetchEtalase(1987772))
  }

  render() {
    const products = this.props.products.items
    const fetchInProgress = this.props.products.isFetching
    const etalases = this.props.etalases.items
    const refreshing = this.props.products.refreshing
    const selectedEtalase = this.props.etalases.selected

    return (
      <View style={styles.container}>
        <View style={styles.productListHeader}>
          <Text style={styles.etalaseText}>Etalase: </Text>
          <StorefrontPicker
            value={selectedEtalase}
            onChange={this.onPickerChange}
            isFetching={fetchInProgress}
            options={etalases} />
        </View>
        {products.length > 0 && <FlatList
          data={products}
          keyExtractor={item => item.id}
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

  return {
    products,
    etalases
  }
}

const styles = StyleSheet.create({
  container: {
   flex: 1,
   width: '90%',
  },
  productListHeader: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'flex-end',
    marginTop: 14,
    marginBottom: 14,
    paddingRight: 5
  },
  etalaseText: {
    fontWeight: 'bold',
  }
})

export default connect(mapStateToProps)(VisibleProductList)