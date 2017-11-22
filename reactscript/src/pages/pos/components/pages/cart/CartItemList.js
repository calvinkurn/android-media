import React, { Component } from 'react'
import { Modal, View, TouchableWithoutFeedback, Image, ScrollView } from 'react-native'
import CartItem from './CartItem'
import Button from '../../../common/TKPPrimaryBtn'
import PopUp from '../../../common/TKPPopupModal'
import { Text } from '../../../common/TKPText'
import Processing from '../../Processing'
import { NavigationModule } from 'NativeModules'
import { connect } from 'react-redux'
import { PaymentCheckoutToNative, GetPaymentRate } from '../../../actions/index'
import { icons } from '../../../lib/config'
import numeral from 'numeral'



class CartItemList extends Component {
  constructor(props) {
    super(props)
    this.state = {
      showPopUp: false,
    }
  }

  componentWillMount() {
    this.props.fetchCartList()
    this.props.dispatch(GetPaymentRate())
  }

  toggleScreen = (visible) => {
    this.setState({ showPopUp: visible })
  }

  remove = () => {
    this.setState({ showPopUp: false })
    this.props.onRemoveAllFromCart()
  }

  paymentCheckoutClicked = () => {
    this.props.dispatch(PaymentCheckoutToNative())
  }

  onBackPress = () => {
    NavigationModule.navigateAndFinish("posapp://product", "")
  }

  

  render() {
    const items = this.props.items
    const visible = this.props.visible
    const onBackPress = this.props.onBackPress
    const totalPrice = this.props.totalPrice
    // let totalPriceWithCurrency = totalPrice.toLocaleString('id-ID')
    const totalPriceWithCurrency = numeral(totalPrice).format('0,0');
    const onIncrQty = this.props.onIncrQty
    const onDecrQty = this.props.onDecrQty
    const onRemoveFromCart = this.props.onRemoveFromCart
    const onRemoveAllFromCart = this.props.onRemoveAllFromCart
    const { isFetching } = this.props

    if (this.props.checkout_showLoadingPage){
      return <Processing />
    }

    const {
      checkout_status_msg,
      showLoadingPage,
      isFetchingParamsCheckout,
    } = this.props
    if (checkout_status_msg === 'SUCCESS' && !showLoadingPage && !isFetchingParamsCheckout){
      console.log('cart item list')
      NavigationModule.navigateAndFinish(`posapp://payment/checkout?checkout_data=${JSON.stringify(this.props.checkout_data)}`, "")
    }


    return (
      <View>
        {
          isFetching ? null : (
            items && items.length > 0 ? (
              !isFetching && (<View>
                <View style={styles.headerContainer}>
                  <View>
                    <TouchableWithoutFeedback onPress={this.onBackPress}>
                      <Image source={{ uri: icons.icon_back }} style={{resizeMode:'contain', width:25, height:25 }} />
                    </TouchableWithoutFeedback>
                  </View>
                  <View style={{ left: -220 }}>
                    <Text style={{ fontSize: 20, color: '#fff', fontWeight: '300' }}>Keranjang Belanja</Text>
                  </View>
                  <View>
                    <TouchableWithoutFeedback onPress={() => { this.toggleScreen(true) }}>
                      <Image source={{ uri: icons.trash_all }} style={{resizeMode:'contain', width:25, height:25 }} />
                    </TouchableWithoutFeedback>
                  </View>
                </View>
                <View>
                  <PopUp
                    visible={this.state.showPopUp}
                    animationType='none'
                    onBackPress={() => { this.toggleScreen(false) }}
                    title='Konfirmasi Pembatalan'
                    subTitle='Apakah Anda yakin untuk menghapus semua daftar belanjaan?'
                    onSecondOptionTap={this.remove}
                    onFirstOptionTap={() => { this.toggleScreen(false) }}
                    firstOptionText='Tidak'
                    secondOptionText='Ya'
                    onCloseIconTap={() => { this.toggleScreen(false) }}
                  />
                </View>
                <View style={{ marginTop: 32, marginLeft: 32, marginRight: 32 }}>
                  <ScrollView style={styles.itemListContainer}>
                    {
                      items.map(i => <CartItem
                        item={i}
                        key={i.id}
                        onIncr={onIncrQty}
                        onDecr={onDecrQty}
                        onRemove={onRemoveFromCart}
                      />)
                    }
                  </ScrollView>
                </View>
                <View style={styles.paymentContainer}>
                  <Text style={styles.paymentText}>Total Pembayaran</Text>
                  <Text style={styles.paymentText}>Rp {totalPriceWithCurrency}</Text>
                </View>
                <View style={{ marginTop: 16, marginLeft: 32, marginRight: 32 }}>
                  <Button
                    content='Checkout'
                    type='extraBigNormal'
                    onTap={() => { this.paymentCheckoutClicked() }}
                  />
                </View>
              </View>)
            ) :
              <View>
                <View style={styles.headerContainer}>
                  <View>
                    <TouchableWithoutFeedback onPress={this.onBackPress}>
                      <Image source={{ uri: icons.icon_back }} style={{resizeMode:'contain', width:25, height:25 }} />
                    </TouchableWithoutFeedback>
                  </View>
                  <View style={{ left: -220 }}>
                    <Text style={{ fontSize: 20, color: '#fff', fontWeight: '300' }}>Keranjang Belanja</Text>
                  </View>
                  <View></View>
                </View>
                <View style={styles.emptyList}>
                  <View style={{
                    alignItems: 'center',
                    justifyContent: 'center',
                    backgroundColor: '#fff',
                    borderRadius: 5
                  }}>
                    <Image source={{ uri: icons.shopping_basket }}
                      style={{ width: 200, height: 200, marginBottom: 15, marginTop: 50 }} />
                    <Text style={{ marginBottom: 50 }}>Tidak Ada barang dalam keranjang</Text>
                  </View>
                </View>
                <View style={{ marginTop: 20, marginLeft: 32, marginRight: 32 }}>
                    <Button
                      content='Mulai Belanja'
                      type='big'
                      onTap={this.onBackPress}
                    />
                  </View>
              </View>
          )
        }
      </View>
    )

    
  }
}

const styles = {
  paymentContainer: {
    marginTop: 16,
    marginLeft: 32,
    marginRight: 32,
    borderRadius: 5,
    flexDirection: 'row',
    backgroundColor: '#fff',
    justifyContent: 'space-between',
    paddingVertical: 20,
    paddingHorizontal: 20,
    borderWidth: 1,
    borderColor: '#e0e0e0'
  },
  itemListContainer: {
    backgroundColor: '#fff',
    borderRadius: 5,
    borderWidth: 1,
    borderColor: '#e0e0e0'
  },
  emptyList: {
    marginLeft: 32,
    marginRight: 32,
    marginTop: 32,
    // paddingVertical: 10,
    // paddingHorizontal: 15,
    // backgroundColor: '#f1f1f1',
    // flex: 1
  },
  emptyContainer: {
    flex: 1,
  },
  container: {
    flex: 1,
    backgroundColor: '#f1f1f1',
    paddingVertical: 30,
    paddingHorizontal: '4%',
    backgroundColor: '#f1f1f1',
    borderWidth: 1,
    borderColor: '#e0e0e0'
  },
  checkoutBtn: {
    height: 40,
    backgroundColor: '#ff5722',
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 3,
    borderWidth: 1,
    borderColor: '#ff5722',
  },
  checkoutBtnText: {
    color: '#fff',
    fontSize: 13,
    fontWeight: '600'
  },
  headerContainer: {
    height: 75,
    backgroundColor: '#42b549',
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 32,
  },
  headerTitleText: {
    fontSize: 24,
    color: '#fff',
    fontWeight: '300',
  },
  paymentText: {
    fontSize: 16,
    color: 'black'
  }
}


const mapStateToProps = (state) => {
    // console.log(state)
    return {
      checkout_isFetchingParamsCheckout: state.checkout.isFetchingParamsCheckout,
      checkout_showLoadingPage: state.checkout.showLoadingPage,
      checkout_data: state.checkout.data,
      checkout_status_msg: state.checkout.status_msg
    }
}

export default connect(mapStateToProps)(CartItemList)