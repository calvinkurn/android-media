import React, { Component } from 'react'
import { Modal, View, TouchableWithoutFeedback, Image, ScrollView } from 'react-native'
import CartItem from './CartItem'
import Button from '../../common/TKPPrimaryBtn'
import PopUp from '../../common/TKPPopupModal'
import { Text } from '../../common/TKPText'
import { NavigationModule } from 'NativeModules'


export default class CartItemList extends Component {
  constructor(props) {
    super(props)
    this.state = {
      showPopUp: false
    }
  }

  toggleScreen = (visible) => {
    this.setState({ showPopUp: visible })
  }

  remove = () => {
    this.setState({ showPopUp: false })
    this.props.onRemoveAllFromCart()
  }

  paymentCheckoutClicked = () => {
    console.log("checkout clicked, total: " + this.props.totalPrice)
    NavigationModule.navigateAndFinish(`posapp://payment/checkout?total_payment=${this.props.totalPrice}`, "")
  }

  onBackPress = () => {
    NavigationModule.navigateAndFinish("posapp://product", "")
  }
  componentWillMount() {
    this.props.fetchCartList()
  }

  render() {
    const items = this.props.items
    const visible = this.props.visible
    const onBackPress = this.props.onBackPress
    const totalPrice = this.props.totalPrice
    const onIncrQty = this.props.onIncrQty
    const onDecrQty = this.props.onDecrQty
    const onRemoveFromCart = this.props.onRemoveFromCart
    const onRemoveAllFromCart = this.props.onRemoveAllFromCart
    const { isFetching } = this.props

    console.log(this.props)

    // return (
    //   <View>
        
    //   </View>
    // )

    // return (
    //   <View>
    //     <View>
    //             <View style={styles.headerContainer}>
    //               <View>
    //                 <TouchableWithoutFeedback onPress={this.onBackPress}>
    //                   <Image source={require('../img/icon_back.png')} />
    //                 </TouchableWithoutFeedback>
    //               </View>
    //               <View style={{ left: -220 }}>
    //                 <Text style={{ fontSize: 20, color: '#fff', fontWeight: '300' }}>Keranjang Belanja</Text>
    //               </View>
    //               <View>
    //                 <TouchableWithoutFeedback onPress={() => { this.toggleScreen(true) }}>
    //                   <Image source={require('../img/trash-all.png')} />
    //                 </TouchableWithoutFeedback>
    //               </View>
    //             </View>
    //             {/* <View style={styles.container}> */}
    //               <View>
    //                 <PopUp
    //                   visible={this.state.showPopUp}
    //                   animationType='fade'
    //                   onBackPress={() => { this.toggleScreen(false) }}
    //                   title='Konfirmasi Pembatalan'
    //                   subTitle='Apakah Anda yakin untuk menghapus semua daftar belanjaan?'
    //                   onSecondOptionTap={this.remove}
    //                   onFirstOptionTap={() => { this.toggleScreen(false) }}
    //                   firstOptionText='Tidak'
    //                   secondOptionText='Ya'
    //                   onCloseIconTap={() => { this.toggleScreen(false) }}
    //                 />
    //               </View>
    //               <View style={{ marginTop: 20, marginLeft: 20, marginRight: 20 }}>
    //                 <ScrollView style={styles.itemListContainer}>
    //                   {
    //                     items.map(i => <CartItem
    //                       item={i}
    //                       key={i.id}
    //                       onIncr={onIncrQty}
    //                       onDecr={onDecrQty}
    //                       onRemove={onRemoveFromCart}
    //                     />)
    //                   }
    //                 </ScrollView>
    //               </View>
    //               <View style={styles.paymentContainer}>
    //                 <Text style={styles.paymentText}>Total Pembayaran</Text>
    //                 <Text style={styles.paymentText}>Rp {(totalPrice).toLocaleString("id")}</Text>
    //               </View>
    //               <View style={{ marginTop: 20, marginLeft: 20, marginRight: 20, }}>
    //                 <Button
    //                   content='Checkout'
    //                   type='big'
    //                   onTap={() => { this.paymentCheckoutClicked() }}
    //                 />
    //               </View>
    //             {/* </View> */}
    //           </View>
    //     {/* {
    //       isFetching ? null : (
    //         items && items.length > 0 ? (
    //           !isFetching && (<View style={{ flex: 1 }}>
    //             <View style={styles.headerContainer}>
    //               <View>
    //                 <TouchableWithoutFeedback onPress={this.onBackPress}>
    //                   <Image source={require('../img/icon_back.png')} />
    //                 </TouchableWithoutFeedback>
    //               </View>
    //               <View style={{ left: -220 }}>
    //                 <Text style={{ fontSize: 20, color: '#fff', fontWeight: '300' }}>Keranjang Belanja</Text>
    //               </View>
    //               <View>
    //                 <TouchableWithoutFeedback onPress={() => { this.toggleScreen(true) }}>
    //                   <Image source={require('../img/trash-all.png')} />
    //                 </TouchableWithoutFeedback>
    //               </View>
    //             </View>
    //             <View style={styles.container}>
    //               <View>
    //                 <PopUp
    //                   visible={this.state.showPopUp}
    //                   animationType='fade'
    //                   onBackPress={() => { this.toggleScreen(false) }}
    //                   title='Konfirmasi Pembatalan'
    //                   subTitle='Apakah Anda yakin untuk menghapus semua daftar belanjaan?'
    //                   onSecondOptionTap={this.remove}
    //                   onFirstOptionTap={() => { this.toggleScreen(false) }}
    //                   firstOptionText='Tidak'
    //                   secondOptionText='Ya'
    //                   onCloseIconTap={() => { this.toggleScreen(false) }}
    //                 />
    //               </View>
    //               <ScrollView style={styles.itemListContainer}>
    //                 {
    //                   items.map(i => <CartItem
    //                     item={i}
    //                     key={i.id}
    //                     onIncr={onIncrQty}
    //                     onDecr={onDecrQty}
    //                     onRemove={onRemoveFromCart}
    //                   />)
    //                 }
    //               </ScrollView>
    //               <View style={styles.paymentContainer}>
    //                 <Text style={styles.paymentText}>Total Pembayaran</Text>
    //                 <Text style={styles.paymentText}>Rp {(totalPrice).toLocaleString("id")}</Text>
    //               </View>
    //               <View style={{ marginTop: 20 }}>
    //                 <Button
    //                   content='Checkout'
    //                   type='big'
    //                   onTap={() => { this.paymentCheckoutClicked() }}
    //                 />
    //               </View>
    //             </View></View>)
    //         ) :
    //           <View style={styles.emptyContainer}>
    //             <View style={styles.headerContainer}>
    //               <View>
    //                 <TouchableWithoutFeedback onPress={this.onBackPress}>
    //                   <Image source={require('../img/icon_back.png')} />
    //                 </TouchableWithoutFeedback>
    //               </View>
    //               <View style={{ left: -220 }}>
    //                 <Text style={{ fontSize: 20, color: '#fff', fontWeight: '300' }}>Keranjang Belanja</Text>
    //               </View>
    //               <View></View>
    //             </View>
    //             <View style={styles.emptyList}>
    //               <View style={{
    //                 alignItems: 'center',
    //                 justifyContent: 'center',
    //                 height: '40%',
    //                 backgroundColor: '#fff'
    //               }}>
    //                 <Image source={require('../img/shopping-basket.png')} style={{ width: 100, height: 100 }} />
    //                 <Text>Tidak Ada barang dalam keranjang</Text>
    //               </View>
    //               <View style={{ marginTop: 20 }}>
    //                 <Button
    //                   content='Mulai Belanja'
    //                   type='big'
    //                   onTap={this.onBackPress}
    //                 />
    //               </View>
    //             </View>
    //           </View>
    //       )
    //     } */}
    //   </View>
    // )



    
    return (
      <View>
        {
          isFetching ? null : (
            items && items.length > 0 ? (
              !isFetching && (<View>
                <View style={styles.headerContainer}>
                  <View>
                    <TouchableWithoutFeedback onPress={this.onBackPress}>
                      <Image source={require('../img/icon_back.png')} />
                    </TouchableWithoutFeedback>
                  </View>
                  <View style={{ left: -220 }}>
                    <Text style={{ fontSize: 20, color: '#fff', fontWeight: '300' }}>Keranjang Belanja</Text>
                  </View>
                  <View>
                    <TouchableWithoutFeedback onPress={() => { this.toggleScreen(true) }}>
                      <Image source={require('../img/trash-all.png')} />
                    </TouchableWithoutFeedback>
                  </View>
                </View>
                  <View>
                    <PopUp
                      visible={this.state.showPopUp}
                      animationType='fade'
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
                  <View style={{ marginTop: 20, marginLeft: 20, marginRight: 20 }}>
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
                    <Text style={styles.paymentText}>Rp {(totalPrice).toLocaleString("id")}</Text>
                  </View>
                  <View style={{ marginTop: 20, marginLeft: 20, marginRight: 20 }}>
                    <Button
                      content='Checkout'
                      type='big'
                      onTap={() => { this.paymentCheckoutClicked() }}
                    />
                  </View>
                </View>)
            ) :
              <View>
                {/* <View style={styles.headerContainer}>
              <View style={{ width: '10%', left: 10 }}>
                <TouchableWithoutFeedback onPress={onBackPress}>
                  <Image source={require('../img/icon_back.png')} />
                </TouchableWithoutFeedback>
              </View>
              <View style={{left: -220}}>
                <Text style={{ fontSize: 20, color: '#fff', fontWeight: '300' }}>Keranjang Belanja</Text>
              </View>
            </View> */}
                <View style={styles.headerContainer}>
                  <View>
                    <TouchableWithoutFeedback onPress={this.onBackPress}>
                      <Image source={require('../img/icon_back.png')} />
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
                    <Image source={require('../img/shopping-basket.png')} 
                      style={{ width: 200, height: 200, marginBottom: 15, marginTop: 50 }} />
                    <Text style={{ marginBottom: 50 }}>Tidak Ada barang dalam keranjang</Text>
                  </View>
                </View>
                <View style={{ marginTop: 20, marginLeft: 20, marginRight: 20 }}>
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
    marginTop: 10, marginLeft: 20, marginRight: 20,
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
    borderWidth: 1,
    borderColor: '#e0e0e0'
  },
  emptyList: {
    marginLeft: 20,
    marginRight: 20,
    marginTop: 20,
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
    paddingHorizontal: '4%',
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